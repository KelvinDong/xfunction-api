package net.xfunction.java.api.modules.meeting.pojo;

import java.util.List;

import lombok.Data;

@Data
public class MeetingToken {

	String appid;
	String userid;
	List<String> gslb;
	String channelId ;
	String token;
	String nonce;
	Long timestamp;
	
	String channelName;
	String userName;
	String currentLayout;
	
	String mqttToken;
	
}
