package net.xfunction.java.api.core.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.*;


@Slf4j
@Service
public class MyHandler implements WebSocketHandler {


    private static final Map<String, WebSocketSession> ACTIVITIES;

    static {
    	ACTIVITIES = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String id = (String) session.getAttributes().get("WEBSOCKET_ACTIVITY_ID");
        log.debug("establish socket:"+id);
        if (id != null) {
        	ACTIVITIES.put(id, session);
        }
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JSONObject msg = JSON.parseObject(message.getPayload().toString());
        /*
        JSONObject obj = new JSONObject();
        String type = msg.get("type").toString();
        if (StringUtils.isNotBlank(type) && SEND_ALL.equals(type)) {
            //给所有人
            obj.put("msg", msg.getString("msg"));
            sendMessageToUsers(new TextMessage(obj.toJSONString()));
        } else {
            //给个人
            String to = msg.getString("to");
            obj.put("msg", msg.getString("msg"));
            sendMessageToUser(to, new TextMessage(obj.toJSONString()));
        }
        */
    }
    

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }        
        String activityId = getActivityId(session);
        if (ACTIVITIES.get(activityId) != null) {
        	ACTIVITIES.remove(activityId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    	String activityId = getActivityId(session);
        if (ACTIVITIES.get(activityId) != null) {
        	ACTIVITIES.remove(activityId);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String getActivityId(WebSocketSession session) {
        try {
            return (String) session.getAttributes().get("WEBSOCKET_ACTIVITY_ID");
        } catch (Exception e) {
            return null;
        }
    }

    /*
    public void sendMessageToUsers(TextMessage message) {
        WebSocketSession activity = null;
        for (String key : ACTIVITIES.keySet()) {
        	activity = ACTIVITIES.get(key);
            try {
                if (activity.isOpen()) {
                	activity.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	*/

    public void sendMessageToUser(String activityId, TextMessage message) {
        WebSocketSession activity = ACTIVITIES.get(activityId);
        log.debug("sending:" + activityId);
        try {
            if (activity.isOpen()) {
            	activity.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public void logout(String activityId) {
    	ACTIVITIES.remove(activityId);
    }
    */
}