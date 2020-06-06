package net.xfunction.java.api.modules.user.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.startup.UserDatabase;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.exception.BizException;
import net.xfunction.java.api.core.pojo.Sms;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.RedisService;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.core.utils.SmsService;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorVo;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;
import net.xfunction.java.api.modules.user.pojo.UserDto;
import net.xfunction.java.api.modules.user.pojo.UserResumeUpdateVo;
import net.xfunction.java.api.modules.user.pojo.WxQuery;
import net.xfunction.java.api.modules.user.query.SlideQuery;
import net.xfunction.java.api.modules.user.query.UserQuery;
import net.xfunction.java.api.modules.user.service.AuthHelper;
import net.xfunction.java.api.modules.user.service.SlideService;
import net.xfunction.java.api.modules.user.service.TokenService;
import net.xfunction.java.api.modules.user.service.UserBaseService;
import net.xfunction.java.api.modules.user.service.WxService;
import net.xfunction.java.api.modules.user.service.WxService2;

//control层,,针以update,如果直接以数据库实体对像进行传参，很容易让用户直接修改表中所有的内容。所以尽量需要一个转换
	// Service层，过的参数，虽然过来的是数据库实体对象，也建议进行必有过滤。主要针对Update。
	
	// 为保证以上措施，可以强制要求 不能使用与数据库一样的命名方式，去掉 xfu前缀。

@RestController
@Slf4j
@RequestMapping("/user/base")
public class UserBaseController {
	
	@Resource
	private RedisService redisService;
	
	@Resource
	private AuthHelper authHelper;
	
	@Resource 
	private UserBaseService  userBaseService;
	
	@Resource
	private TokenService tokenService;
	
	@Resource SlideService slideService;
	
	@Resource
	private SmsService smsService;
	
	@Resource
	private WxService wxService;
	
	@Resource
	private WxService2 wxService2;
	
	//20191227 调整为手机号验证码登录及注册（登录即注册）
	
	@PostMapping("/sendLoginCode")
	public Result sendLoginCode(@RequestBody Sms sms, HttpServletRequest httpServletRequest) {
		if(!BaseUtils.isPhone(sms.getMobile())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		Integer rnd = (int) ((Math.random()*9+1)*100000);
		sms.setCode(rnd.toString().substring(0, 4));
		redisService.set("login"+sms.getMobile(), sms.getCode(), Duration.ofSeconds(300).getSeconds());		
		smsService.sendSms(sms);
		return Result.success();
	}
	
	@PostMapping("/userLogin")
	public Result userLoing(@RequestBody UserQuery query) {
		String saveCode = (String) redisService.get("login"+query.getUserMobile());
		if(BaseUtils.isEmpty(saveCode)
				|| !saveCode.equals(query.getCode())) {
			Result.failure(ResultCodeEnum.PARAM_ERROR);
		}
		XfuUser user = new XfuUser();
		user.setXfuUserMobile(query.getUserMobile());
		return userBaseService.getUser(user);
	}
	
	@PostMapping("/get")
	@UserLoginToken
	public Result get(HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.FAILD);
		XfuUser user = new XfuUser();
		user.setXfuUserId(userIdLong);	
		return userBaseService.getUser(user);
	}
	
	@PostMapping("/picUser")
	@UserLoginToken
	public Result picUser(UserResumeUpdateVo vo,HttpServletRequest httpServletRequest) throws IllegalStateException, IOException {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(BaseUtils.isNull(vo.getFile())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		XfuUser user = vo.convertToXfuUser();
		user.setXfuUserId(userIdLong);
		return Result.success(userBaseService.saveUserPic(user,vo.getFile()));
	}
	
	@PostMapping("/updateResume")
	@UserLoginToken
	public Result updateResume(@RequestBody UserResumeUpdateVo resume , HttpServletRequest httpServletRequest) {
		log.info(resume.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);		
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		XfuUser user = resume.convertToXfuUser();
		user.setXfuUserId(userIdLong);	
		return userBaseService.updateUser(user);
	}
	
	
	/*
	@PostMapping("/register")
	public Result register(@RequestBody UserQuery userQuery,HttpServletRequest httpServletRequest) {
		
		SlideQuery slideQuery =  new SlideQuery();
		slideQuery.setAction(userQuery.getAction());
		slideQuery.setMove(userQuery.getMove());		
		if(!slideService.vertify(slideQuery, httpServletRequest)) {
			return Result.failure(ResultCodeEnum.FAILD,"拼图验证失败");
		}
		
		XfuUser user = new XfuUser();
		user.setXfuUserName(userQuery.getUserName());
		user.setXfuUserAuth(userQuery.getUserAuth());						
		if(!BaseUtils.isName(user.getXfuUserName()) || !BaseUtils.isPassword(user.getXfuUserAuth()) ) {
			return Result.failure(ResultCodeEnum.PARAM_ERROR);
		}
		
		return userBaseService.createUser(user);

	}
	
	
	@PostMapping("/login")
	public Result login(@RequestBody UserQuery userQuery,HttpServletRequest httpServletRequest ) { 
		
		SlideQuery slideQuery =  new SlideQuery();
		slideQuery.setAction(userQuery.getAction());
		slideQuery.setMove(userQuery.getMove());	
		if(!slideService.vertify(slideQuery, httpServletRequest)) {
			return Result.failure(ResultCodeEnum.FAILD,"拼图验证失败");
		}		
		// 允许用户名
		if(!BaseUtils.isPassword(userQuery.getUserAuth())
				|| !BaseUtils.isName(userQuery.getUserName())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		XfuUser user = new XfuUser();
		user.setXfuUserName(userQuery.getUserName());
		user.setXfuUserAuth(userQuery.getUserAuth());	
		
		return userBaseService.findUserByAuth(user);
	}
	
	@PostMapping("/sendResetCode")
	public Result sendResetCode(@RequestBody Sms sms, HttpServletRequest httpServletRequest) {
		log.info(sms.toString());
		SlideQuery slideQuery =  new SlideQuery();
		slideQuery.setAction(sms.getAction());
		slideQuery.setMove(sms.getMove());	
		slideQuery.setClear(false);   //不能删除 
		if(!slideService.vertify(slideQuery, httpServletRequest)) {
			return Result.failure(ResultCodeEnum.FAILD,"拼图验证失败");
		}
		
		if(!BaseUtils.isPhone(sms.getMobile())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		XfuUser exist = userBaseService.isMobleExist(sms.getMobile());
		
		if(BaseUtils.isNull(exist)) {
			return Result.failure(ResultCodeEnum.FAILD,"手机号未绑定任何帐号");
		}
		
		Integer rnd = (int) ((Math.random()*9+1)*100000);
		sms.setCode(rnd.toString().substring(0, 6));
		redisService.set("reset"+sms.getMobile(), sms.getCode(), Duration.ofSeconds(300).getSeconds());		
		//System.out.println(sms.toString());
		smsService.sendSms(sms);
		return Result.success(BaseUtils.maskUserName(exist.getXfuUserName()));
	}
	
	@PostMapping("/resetUser")
	public Result resetUser(@RequestBody UserQuery query,HttpServletRequest httpServletRequest) {
		
		SlideQuery slideQuery =  new SlideQuery();
		slideQuery.setAction(query.getAction());
		slideQuery.setMove(query.getMove());	
		// slideQuery.setClear(true);   //默认是删
		if(!slideService.vertify(slideQuery, httpServletRequest)) {
			return Result.failure(ResultCodeEnum.FAILD,"拼图验证失败");
		}
		if(!BaseUtils.isPhone(query.getUserMobile()) || BaseUtils.isNull(query.getCode())
				|| !BaseUtils.isPassword(query.getUserAuth())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		
		String saveCode = (String) redisService.get("reset"+query.getUserMobile());
		if(BaseUtils.isNull(saveCode) ||
				!query.getCode().equals(saveCode))
			return Result.failure(ResultCodeEnum.FAILD,"短信验证码校验失败");
		
		XfuUser exist = userBaseService.isMobleExist(query.getUserMobile());
		
		if(BaseUtils.isNull(exist)) {
			return Result.failure(ResultCodeEnum.FAILD,"手机号未绑定任何帐号");
		}
		
		XfuUser user = new XfuUser();
		user.setXfuUserId(exist.getXfuUserId());
		user.setXfuUserAuth(query.getUserAuth());
		user.setXfuUserAuth(authHelper.encryptAuthBysalt(query.getUserAuth(),exist.getXfuUserAuthSalt()));
		user.setXfuUserUpdateDate(new Date());
		return userBaseService.updateUser(user);
		
	}
	

	@PostMapping("/bindResult")
	public Result bindResult(@RequestBody UserQuery userQuery) {
		
		if(BaseUtils.isEmpty(userQuery.getToken())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		return userBaseService.bindResult(userQuery);
	}
	
	@PostMapping("/reset")
	public Result reset(@RequestBody UserQuery userQuery,HttpServletRequest httpServletRequest ) { 
		
		SlideQuery slideQuery =  new SlideQuery();
		slideQuery.setAction(userQuery.getAction());
		slideQuery.setMove(userQuery.getMove());	
		if(!slideService.vertify(slideQuery, httpServletRequest)) {
			return Result.failure(ResultCodeEnum.FAILD,"验证失败");
		}
		
		if(!BaseUtils.isMail(userQuery.getUserMail())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		
		XfuUser user = new XfuUser();
		user.setXfuUserMail(userQuery.getUserMail());
		return userBaseService.sendPasswordMail(user);
		
	}
	
	@PostMapping("resetResult")
	public Result resetResult(@RequestBody UserQuery userQuery,HttpServletRequest httpServletRequest ) { 
		SlideQuery slideQuery =  new SlideQuery();
		slideQuery.setAction(userQuery.getAction());
		slideQuery.setMove(userQuery.getMove());	
		if(!slideService.vertify(slideQuery, httpServletRequest)) {
			return Result.failure(ResultCodeEnum.FAILD,"验证失败");
		}
		
		if(!BaseUtils.isPassword(userQuery.getUserAuth()) 
				|| BaseUtils.isEmpty(userQuery.getToken())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		
		return userBaseService.resetFromMail(userQuery);
		
	}*/
	
	//////////////////////////以下均要登录//////////////////////////////////////////////////
	/*
	@PostMapping("/sendBindCode")
	@UserLoginToken
	public Result sendBindCode(@RequestBody Sms sms,HttpServletRequest httpServletRequest ) { 
		log.info(sms.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(!BaseUtils.isPhone(sms.getMobile())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		XfuUser exist = userBaseService.isMobleExist(sms.getMobile());
		
		if(BaseUtils.isNotNull(exist)) {
			if(exist.getXfuUserId().equals(userIdLong)) 
				return Result.failure(ResultCodeEnum.FAILD,"已经被你绑定，无须重复操作");
			else
				return Result.failure(ResultCodeEnum.FAILD,"已经被他/她人绑定，无法操作");
		}
		
		Integer rnd = (int) ((Math.random()*9+1)*100000);
		sms.setCode(rnd.toString().substring(0, 6));
		redisService.set("bind"+sms.getMobile(), sms.getCode(), Duration.ofSeconds(300).getSeconds());		
		smsService.sendSms(sms);
		return Result.success();
	}
	
	@PostMapping("/activeMobile")
	@UserLoginToken
	public Result activeMobile(@RequestBody Sms sms,HttpServletRequest httpServletRequest) {
		log.info(sms.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(!BaseUtils.isPhone(sms.getMobile()) || BaseUtils.isNull(sms.getCode())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		
		String saveCode = (String) redisService.get("bind"+sms.getMobile());
		if(BaseUtils.isNull(saveCode) ||
				!sms.getCode().equals(saveCode))
			return Result.failure(ResultCodeEnum.FAILD,"短信验证码校验失败");
		
		XfuUser exist = userBaseService.isMobleExist(sms.getMobile());
		if(BaseUtils.isNotNull(exist)) {
			if(exist.getXfuUserId().equals(userIdLong)) 
				return Result.failure(ResultCodeEnum.FAILD,"已经被你绑定，无须重复操作");
			else
				return Result.failure(ResultCodeEnum.FAILD,"已经被他/她人绑定，无法操作");
		}
		
		XfuUser save = new XfuUser();
		save.setXfuUserId(userIdLong);
		save.setXfuUserMobile(sms.getMobile());
		userBaseService.updateUser(save);
		
		return Result.success();
	}
	
	
	
	
	@PostMapping("/sendChangeCode")
	@UserLoginToken
	public Result sendChangeCode(HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.FAILD);
		//找到用户，以获取手机号
		XfuUser user = new XfuUser();
		user.setXfuUserId(userIdLong);	
		user = userBaseService.getXfuUser(user);
		if(BaseUtils.isNotNull(user)) {
			if(BaseUtils.isPhone(user.getXfuUserMobile())) {
				Sms sms = new Sms();
				sms.setMobile(user.getXfuUserMobile());
				Integer rnd = (int) ((Math.random()*9+1)*100000);
				sms.setCode(rnd.toString().substring(0, 6));
				redisService.set("c_auth"+sms.getMobile(), sms.getCode(), Duration.ofSeconds(300).getSeconds());		
				//System.out.println(sms.toString());
				smsService.sendSms(sms);
				return Result.success();
			}else {
				return Result.failure(ResultCodeEnum.FAILD,"请先完成手机号绑定");
			}
			
		}else {
			return Result.failure(ResultCodeEnum.FAILD);
		}

	}
	
	@PostMapping("/changeAuth")
	@UserLoginToken
	public Result changeAuth(@RequestBody UserQuery userQuery , HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(!BaseUtils.isPassword(userQuery.getUserAuth()) 
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		XfuUser user = new XfuUser();
		user.setXfuUserId(userIdLong);	
		XfuUser exist = userBaseService.getXfuUser(user);
		if(BaseUtils.isNotNull(exist)) {
			if(BaseUtils.isPhone(exist.getXfuUserMobile())) {
				String saveCode = (String) redisService.get("c_auth"+exist.getXfuUserMobile());
				if(BaseUtils.isNull(saveCode) ||
						!userQuery.getCode().equals(saveCode))
					return Result.failure(ResultCodeEnum.FAILD,"短信验证码校验失败");
				exist.setXfuUserAuth(userQuery.getUserAuth());	
				userBaseService.updateUserByAuth(exist);
				return Result.success();
			}else {
				return Result.failure(ResultCodeEnum.FAILD,"请先完成手机号绑定");
			}
		}else {
			return Result.failure(ResultCodeEnum.FAILD);
		}

	}
	
	*/
	
	
	/*
	@PostMapping("/bindMail")
	@UserLoginToken
	public Result bindMail(@RequestBody UserQuery userQuery , HttpServletRequest httpServletRequest) {
		
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(!BaseUtils.isMail(userQuery.getUserMail()) 
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		
		XfuUser user = new XfuUser();
		user.setXfuUserMail(userQuery.getUserMail());			
		user.setXfuUserId(userIdLong);	
		return userBaseService.sendBindMail(user);
	}
	
	*/
	
	@PostMapping("getWxJsParam")
	@UserLoginToken // 微信用于调起扫一扫，也是需要登录的
	public Result getWxJsParam(@RequestBody WxQuery query , HttpServletRequest httpServletRequest) throws Exception {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);		
		if(BaseUtils.isNull(userIdLong)
				|| BaseUtils.isEmpty(query.getUrl())) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		return Result.success(wxService.getWxJsParam(query.getUrl()));
	}
	
	@PostMapping("getWx2JsParam")
	// @UserLoginToken // 微信用于调起扫一扫，也是需要登录的
	public Result getWx2JsParam(@RequestBody WxQuery query , HttpServletRequest httpServletRequest) throws Exception {
		//log.info(query.toString());
		//Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);		
		if(BaseUtils.isEmpty(query.getUrl())) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		return Result.success(wxService2.getWxJsParam(query.getUrl()));
	}
	
	
}
