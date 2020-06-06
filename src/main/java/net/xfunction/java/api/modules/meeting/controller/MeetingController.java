package net.xfunction.java.api.modules.meeting.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.meeting.pojo.UserQuery;
import net.xfunction.java.api.modules.meeting.service.MeetingService;


@RestController
@Slf4j
@RequestMapping("/meeting")

public class MeetingController {

	@Resource 
	private MeetingService meetingService;
	
	@RequestMapping("login")

    public Result getUserToken(UserQuery query, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, InvalidKeyException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, IOException
    {	
		if(BaseUtils.isEmpty(query.getUserAccount())) {// 普通与会者			
			if(BaseUtils.isEmpty(query.getChannelId()) || BaseUtils.isEmpty(query.getChannelCode()) || BaseUtils.isEmpty(query.getUserName())) {
				return Result.failure(ResultCodeEnum.PARAMS_MISS);
			}else {
				return meetingService.getConventionerInfo(query);
			}			
		}else { /// 管理员用户
			if(BaseUtils.isEmpty(query.getChannelId()) || BaseUtils.isEmpty(query.getUserPassword()) ) {
				return Result.failure(ResultCodeEnum.PARAMS_MISS);
			}else {
				return meetingService.getConventionerInfo(query);
			}	
		}
			
    }
	
}
