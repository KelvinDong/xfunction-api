package net.xfunction.java;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriber;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.modules.meeting.pojo.TransMessage;
import net.xfunction.java.api.modules.meeting.utils.TranscriberTokenUtils;


@Slf4j
@Component
@ServerEndpoint("/transcriberSocket/{channelId}")
public class WebSocketServer {

	private static String appKeyCn;	
	private static String appKeyEn;	
	
	@Value("${xfunction.transcriber.appKeyCn}")	
	private void setAppKeyCn(String accessKeyId) {
		WebSocketServer.appKeyCn = accessKeyId;
	}
	
	@Value("${xfunction.transcriber.appKeyEn}")	
	private void setAppKeyEn(String accessKeyId) {
		WebSocketServer.appKeyEn = accessKeyId;
	}
	
	private static NlsClient nlsClient;
	
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private static Producer rocketMq;
    
    private static String rocketGroupId;
    private static String rocketAccessKey;
    private static String rocketAccessSecret;
    private static String rocketServer;
    private static String rocketTopic;    
    private static String rocketPreSubTopic; 
    
	@Value("${xfunction.rocketmq.groupId}")	
    private void setRocketGroupId(String value) {
    	WebSocketServer.rocketGroupId = value;
    }
	@Value("${xfunction.rocketmq.accessKey}")	
    private void setRocketAccessKey(String value) {
    	WebSocketServer.rocketAccessKey = value;
    }
	@Value("${xfunction.rocketmq.secretKey}")	
    private void setRocketAccessSecret(String value) {
    	WebSocketServer.rocketAccessSecret = value;
    }
	@Value("${xfunction.rocketmq.nameSrvAddr}")	
    private void setRocketServer(String value) {
    	WebSocketServer.rocketServer = value;
    }
	@Value("${xfunction.rocketmq.topic}")	
    private void setRocketTopic(String value) {
    	WebSocketServer.rocketTopic = value;
    }
	@Value("${xfunction.rocketmq.subTopic}")	
    private void SetRocketPreSubTopic(String value) {
    	WebSocketServer.rocketPreSubTopic = value;
    }
    
    
    //########   以上均为静态变量   #########
    
    
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    private SpeechTranscriber transcriber = null;

    //接收operatorName
    private String channelId="";
    /**
     * 连接建立成功调用的方法
     * @throws Exception */
    @OnOpen
    public void onOpen(Session session,@PathParam("channelId") String channelId) throws Exception {
    	
    	// 重复登录清理掉
    	WebSocketServer preSocket = webSocketMap.get(channelId);
    	if(!StringUtils.isEmpty(preSocket)) { 
    		webSocketMap.remove(channelId);
    		preSocket.session.close();
    	}
    	
        this.session = session;
        
        log.info("-------"+appKeyCn);
        
        webSocketMap.put(channelId,this);     
        addOnlineCount();           //在线数加1
        this.channelId=channelId;
                
        ///-------------RocketMq发送连接-----------------------------------
        if(rocketMq == null) {
	        Properties properties = new Properties();
	        properties.setProperty(PropertyKeyConst.GROUP_ID, rocketGroupId);
	        properties.put(PropertyKeyConst.AccessKey, rocketAccessKey);
	        properties.put(PropertyKeyConst.SecretKey, rocketAccessSecret);
	        properties.put(PropertyKeyConst.NAMESRV_ADDR, rocketServer);
	        rocketMq = ONSFactory.createProducer(properties);
	        rocketMq.start(); // TODO 未有关闭的地方
        }
        
        
        ///-------------语音引擎连接------------------------
        if(nlsClient == null || TranscriberTokenUtils.getTokenExpired()) { 
        	nlsClient = new NlsClient(TranscriberTokenUtils.getToken());
        }
        transcriber = new SpeechTranscriber(nlsClient, getTranscriberListener());
        transcriber.setAppKey(appKeyCn);
        //输入音频编码方式
        transcriber.setFormat(InputFormatEnum.PCM);
        //输入音频采样率
        transcriber.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
        //是否返回中间识别结果
        transcriber.setEnableIntermediateResult(false);
        //是否生成并返回标点符号
        transcriber.setEnablePunctuation(true);
        //是否将返回结果规整化,比如将一百返回为100
        transcriber.setEnableITN(true);
       
        transcriber.addCustomedParam("max_sentence_silence", 400);
        transcriber.addCustomedParam("enable_semantic_sentence_detection",true);   // 语义断句

        //此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
        transcriber.start();        

    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
    	webSocketMap.remove(this.channelId);  //从set中删除
        subOnlineCount();           //在线数减1        
        try {
			transcriber.stop();     // TODO 未安排 其它方式的退出？？
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (null != transcriber) {
                transcriber.close();
            }
		}        
    }

    /**
     * 收到客户端消息后调用的方法,消息体必须完整，转发出去，用于点对点的【聊天类消息】。
     *
     * @param message 客户端发送过来的消息
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException */
    @OnMessage
    public void onMessage(byte[] message, Session session) throws  IOException {

    	//直接转发给语音引擎
    	transcriber.send(message);
    	
    }

	/**
	 * 
	 * @param session
	 * @param error
	 */
    @OnError
    public void onError(Session session, Throwable error) {

        error.printStackTrace();
    }
	
    
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }    


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
    
    private SpeechTranscriberListener getTranscriberListener() {
        SpeechTranscriberListener listener = new SpeechTranscriberListener() {        	
            //TODO 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onTranscriptionResultChange(SpeechTranscriberResponse response) {
                log.info("task_id: " + response.getTaskId() +
                    ", name: " + response.getName() +
                    //状态码 20000000 表示正常识别
                    ", status: " + response.getStatus() +
                    //句子编号，从1开始递增
                    ", index: " + response.getTransSentenceIndex() +
                    //当前的识别结果
                    ", result: " + response.getTransSentenceText() +
                    //当前已处理的音频时长，单位是毫秒
                    ", time: " + response.getTransSentenceTime());
            }

            @Override
            public void onTranscriberStart(SpeechTranscriberResponse response) {
                // TODO 重要提示： task_id很重要，是调用方和服务端通信的唯一ID标识，当遇到问题时，需要提供此task_id以便排查
                log.info("task_id: " + response.getTaskId() + ", name: " + response.getName() + ", status: " + response.getStatus());
            }

            @Override
            public void onSentenceBegin(SpeechTranscriberResponse response) {
                log.info("task_id: " + response.getTaskId() + ", name: " + response.getName() + ", status: " + response.getStatus());

            }

            //识别出一句话.服务端会智能断句,当识别到一句话结束时会返回此消息
            @Override
            public void onSentenceEnd(SpeechTranscriberResponse response) {
                log.info("task_id: " + response.getTaskId() +
                    ", name: " + response.getName() +
                    //状态码 20000000 表示正常识别
                    ", status: " + response.getStatus() +
                    //句子编号，从1开始递增
                    ", index: " + response.getTransSentenceIndex() +
                    //当前的识别结果
                    ", result: " + response.getTransSentenceText() +
                    //置信度
                    ", confidence: " + response.getConfidence() +
                    //开始时间
                    ", begin_time: " + response.getSentenceBeginTime() +
                    //当前已处理的音频时长，单位是毫秒
                    ", time: " + response.getTransSentenceTime());
                
                TransMessage mq = new TransMessage();
                mq.setType("20");
                mq.setMessage(response.getTransSentenceText());
                Object obj = JSONArray.toJSON(mq);
                
                Message msg = new Message(rocketTopic, "MQ2MQTT", obj.toString().getBytes());
                msg.putUserProperties(PropertyKeyConst.MqttSecondTopic, rocketPreSubTopic + channelId);
                log.debug(msg.toString());
                SendResult result = rocketMq.send(msg);
                log.info(result.toString());
            }

            //识别完毕
            @Override
            public void onTranscriptionComplete(SpeechTranscriberResponse response) {
                log.info("task_id: " + response.getTaskId() + ", name: " + response.getName() + ", status: " + response.getStatus());
            }

            @Override
            public void onFail(SpeechTranscriberResponse response) {
                // TODO 重要提示： task_id很重要，是调用方和服务端通信的唯一ID标识，当遇到问题时，需要提供此task_id以便排查
                log.error("task_id: " + response.getTaskId() +  ", status: " + response.getStatus() + ", status_text: " + response.getStatusText());
            }
        };

        return listener;
    }
    
}
