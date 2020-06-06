package net.xfunction.java.api.modules.meeting.pojo;

import lombok.Data;

@Data
public class UserQuery {

	// 1、普通与会者登录，参数 channelId channelCode userName  userId
	private String channelCode;
	private String userName;  // 用户显示用的名称或 displayName
	private String userId; // 可选参数，由客户端保存在LS中的上次返回的userId,如果没有，由应用重新生成，客户端保存起来。
	
	private String channelId;
	
	// 2、管理员登录， 参数 channelId  userAccount userPassword，也会返回用户userId,
	// 这个是表中 biz_meeting_user_id固定，客户端不要保存在LS中，避免与普通用户的userId产生冲突。
	private String userAccount;
	private String userPassword;
	
	
}
