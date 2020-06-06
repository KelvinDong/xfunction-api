package net.xfunction.java.api.modules.meeting.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;


import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.RedisService;

import java.time.Duration;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class AliRocketMessageService implements MessageListener {

	@Value("${xfunction.cache.meeting}")
	private String meetingKey;		
	@Resource
	private RedisService redisService;
		

    @Override
    public Action consume(Message message, ConsumeContext context) {
        
        try {            
        	
        	// 视频会议控制指令，
        	Long channelId = null;        	
        	String line= message.getUserProperties("mqttSecondTopic");
        	if(line != null) {
        		String pattern="^/meeting/(\\d+).*$";
        		Pattern p=Pattern.compile(pattern);
        		Matcher m=p.matcher(line);
    	    	if(m.find())  channelId = Long.valueOf(m.group(1));
        	}        	

        	if(BaseUtils.isNotNull(channelId)) {
        		
        		JSONObject jSONObj = JSON.parseObject(new String(message.getBody()));
        		
        		if(jSONObj.getString("type").equals("10") )
        		{
        			String cacheKey = meetingKey + "::" + channelId;
    				redisService.set(cacheKey, new String(message.getBody()),60*60*8);
        		}
        		
        	}
        	        	        	
        	//其它丢弃
        	
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
    
       
}


/*
2020-02-15 19:16:22 740465 [ConsumeMessageThread_1] INFO  n.a.j.a.m.m.s.AliRocketMessageService - Message [topic=ACE,
 systemProperties={
	 __KEY=,
	 __RECONSUMETIMES=0,
	 __BORNHOST=/11.193.121.96:36656,
	 __MSGID=0BC1795883F1277050DC4C3BB9CCD131,
	 MIN_OFFSET=0,
	 __BORNTIMESTAMP=1581765382630,
	 MAX_OFFSET=782
 },
 userProperties={
	 MSG_REGION=cn-hangzhou,
	 clientId=GID_ACE@@@userId,
	 UNIQ_KEY=0BC1795883F1277050DC4C3BB9CCD131,
	 TRACE_ON=true,
	 DUP_INFO=482563745_2,
	 CONSUME_START_TIME=1581765382706,
	 qoslevel=0,
	 MSG_S=MQTT,
	 mqttSecondTopic=/live/100
 },
 body=40]


2020-02-15 19:16:22 740466 [ConsumeMessageThread_2] INFO  n.a.j.a.m.m.s.AliRocketMessageService - Message [topic=ACE,
 systemProperties={
	 __KEY=,
	 __RECONSUMETIMES=0,
	 __BORNHOST=/11.193.121.88:41851,
	 __MSGID=0BC1795883F1277050DC4C3BB9CCD132,
	 MIN_OFFSET=0,
	 __BORNTIMESTAMP=1581765382637,
	 MAX_OFFSET=782
 },
 userProperties={
	 MSG_REGION=cn-hangzhou,
	 clientId=GID_ACE@@@userId,
	 UNIQ_KEY=0BC1795883F1277050DC4C3BB9CCD132,
	 TRACE_ON=true,
	 DUP_INFO=482563747_2,
	 CONSUME_START_TIME=1581765382706,
	 qoslevel=0,
	 MSG_S=MQTT,
	 mqttSecondTopic=/live/100/p2p/GID_ACE@@@userId
 },
 body=32]
*/