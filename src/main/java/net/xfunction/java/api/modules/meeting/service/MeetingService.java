package net.xfunction.java.api.modules.meeting.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.RedisService;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.meeting.mapper.xfunction.XfuMeetingAdminMapper;
import net.xfunction.java.api.modules.meeting.mapper.xfunction.XfuMeetingMapper;
import net.xfunction.java.api.modules.meeting.model.xfunction.XfuMeeting;
import net.xfunction.java.api.modules.meeting.model.xfunction.XfuMeetingAdmin;
import net.xfunction.java.api.modules.meeting.pojo.MeetingToken;
import net.xfunction.java.api.modules.meeting.pojo.UserQuery;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Slf4j
public class MeetingService {
	
	
	@Value("${xfunction.ali.mqtt.topic}")
	private String masterTopic;
	private static final String preTopic = "/meeting/";
	
	@Resource
	AliMqttService aliMqttService;
	
	@Value("${xfunction.cache.meeting}")
	private String meetingKey;		
	@Resource
	private RedisService redisService;
	
	
	
	@Value("${xfunction.ali.meeting.appId}")
	private String appId;
	@Value("${xfunction.ali.meeting.appKey}")
	private String appKey;
	@Value("${xfunction.ali.meeting.gslb}")
	private String gslb;
	@Value("${xfunction.ali.meeting.expired}")
	private Integer expired;
	
	@Resource
	private XfuMeetingMapper xfuMeetingMapper;
	
	@Resource 
	private XfuMeetingAdminMapper xfuMeetingAdminMapper;

		
	public Result getConventionerInfo(UserQuery userQuery) throws NoSuchAlgorithmException, InvalidKeyException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, IOException {
		//channelCode  userName channelId  ?userId
		
		if(BaseUtils.isEmpty(userQuery.getUserAccount())) {// 普通与会者	
		
			if(!( BaseUtils.isNotEmpty(userQuery.getUserId()) && userQuery.getUserId().charAt(0) == 'c')) { //存在且c打头的userId
				userQuery.setUserId("c"+createUserID(userQuery.getChannelId(),userQuery.getUserName()));
			}
			
			XfuMeeting meeting = xfuMeetingMapper.selectByPrimaryKey(Integer.parseInt(userQuery.getChannelId()));
			//会议码 会议状态
			if(BaseUtils.isNull(meeting) || !userQuery.getChannelCode().equals(meeting.getXfuChannelCode()) || !meeting.getXfuChannelStatus()) {
				return Result.failure(ResultCodeEnum.PARAM_ERROR,"请检查会议号、会议密码有效性");
			}
			/*
			Weekend<XfuMeeting> example = Weekend.of(XfuMeeting.class);
	        WeekendCriteria<XfuMeeting, Object> criteria = example.weekendCriteria();
	        criteria.andEqualTo();
	        BizUserBaseinfo bizUserBaseinfo = .selectOneByExample(example);
	        */
			return genReturn(userQuery,meeting.getXfuChannelName());
		} else {			
			Weekend<XfuMeetingAdmin> example = Weekend.of(XfuMeetingAdmin.class);
	        WeekendCriteria<XfuMeetingAdmin, Object> criteria = example.weekendCriteria();
	        criteria.andEqualTo(XfuMeetingAdmin::getXfuAdminAccount,userQuery.getUserAccount());
	        criteria.andEqualTo(XfuMeetingAdmin::getXfuAdminPassword,userQuery.getUserPassword());
	        criteria.andEqualTo(XfuMeetingAdmin::getXfuAdminStatus,true);
	        XfuMeetingAdmin admin = xfuMeetingAdminMapper.selectOneByExample(example);
	        if(BaseUtils.isNull(admin)) {
	        	return Result.failure(ResultCodeEnum.PARAM_ERROR,"请检查管理员帐号、密码有效性");
	        }
	        userQuery.setUserId(admin.getXfuMeetingAdminId().toString());
	        userQuery.setUserName(admin.getXfuAdminName());
	        
	        XfuMeeting meeting = xfuMeetingMapper.selectByPrimaryKey(Integer.parseInt(userQuery.getChannelId()));
	        // 会议状态
			if(BaseUtils.isNull(meeting) || !meeting.getXfuChannelStatus()) {
				return Result.failure(ResultCodeEnum.PARAM_ERROR,"请检查会议号有效性");
			}
	        
			return genReturn(userQuery,meeting.getXfuChannelName());
			
		}
	}	
		
	private Result genReturn(UserQuery userQuery,String channelName) throws InvalidKeyException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		//generate token
				Calendar nowTime = Calendar.getInstance();
		        nowTime.add(Calendar.HOUR_OF_DAY, expired);
		        Long timestamp = nowTime.getTimeInMillis() / 1000;
		        String nonce = String.format("AK-%s", UUID.randomUUID().toString());        
		        MessageDigest digest = MessageDigest.getInstance("SHA-256");
		        digest.update(appId.getBytes());
		        digest.update(appKey.getBytes());
		        digest.update(userQuery.getChannelId().getBytes());
		        digest.update(userQuery.getUserId().getBytes());
		        digest.update(nonce.getBytes());
		        digest.update(Long.toString(timestamp).getBytes());
		        String token = DatatypeConverter.printHexBinary(digest.digest()).toLowerCase();
		        // end generate token
		        
		        // return info        
		        MeetingToken meetingToken = new MeetingToken();
		        // 以下为加入视频会议需求的数据
		        meetingToken.setAppid(appId);
		        meetingToken.setUserid(userQuery.getUserId());
		        List<String> gslbList = new  ArrayList<String>();
		        gslbList.add(gslb);
		        meetingToken.setGslb(gslbList);
		        meetingToken.setToken(token);
		        meetingToken.setNonce(nonce);
		        meetingToken.setTimestamp(timestamp);
		        meetingToken.setChannelId(userQuery.getChannelId());
		        
		        meetingToken.setUserName(userQuery.getUserName());
		        meetingToken.setChannelName(channelName);
		        // 获取布局
		        String cacheKey = meetingKey + "::" + userQuery.getChannelId();
				Object cacheValue = redisService.get(cacheKey);
				if (null != cacheValue) {
					meetingToken.setCurrentLayout(cacheValue.toString());
				}
				// 以下生成mqtt的登录凭证
				String topic = masterTopic + preTopic + userQuery.getChannelId();
				topic = topic + "," + masterTopic + "/p2p/#";
				if(BaseUtils.isEmpty(userQuery.getUserAccount())) // 普通与会者	
					meetingToken.setMqttToken(aliMqttService.applyToken(topic, "R"));
				else
					meetingToken.setMqttToken(aliMqttService.applyToken(topic, "R,W"));
		        
		        return Result.success(meetingToken);
	}
	
	private String createUserID(String channelId, String userName) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(channelId.getBytes());
        digest.update("/".getBytes());
        digest.update(userName.getBytes());
        Calendar nowTime = Calendar.getInstance();
        digest.update(Long.toString(nowTime.getTimeInMillis()).getBytes());
        String uid = DatatypeConverter.printHexBinary(digest.digest()).toLowerCase();
        return uid.substring(0, 16);
    }

}
