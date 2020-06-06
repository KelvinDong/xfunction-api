package net.xfunction.java.api.modules.user.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.pojo.Sms;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.user.pojo.ToolPojo;
import net.xfunction.java.api.modules.user.service.TokenService;
import net.xfunction.java.api.modules.user.service.UserToolService;

@RestController
@Slf4j
@RequestMapping("/user/tool")
public class UserToolController {
	
	@Resource
	private TokenService tokenService;
	
	@Resource 
	private UserToolService userToolService;

	@PostMapping("/setSettings")
	@UserLoginToken
	public Result setSettings(@RequestBody ToolPojo pojo,HttpServletRequest httpServletRequest ) { 
		log.info(pojo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if (BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		pojo.setUserId(userIdLong);
		this.userToolService.setToolSettings(pojo);
		return Result.success();
	}
	
	@PostMapping("/getSettings")
	@UserLoginToken
	public Result getSettings(HttpServletRequest httpServletRequest ) { 
		ToolPojo pojo = new ToolPojo();
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if (BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		pojo.setUserId(userIdLong);
		return this.userToolService.getToolSettings(pojo);
	}
}
