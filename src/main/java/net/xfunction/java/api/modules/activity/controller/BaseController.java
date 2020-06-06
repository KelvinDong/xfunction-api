package net.xfunction.java.api.modules.activity.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;
import net.xfunction.java.api.modules.activity.pojo.PicFileQuery;
import net.xfunction.java.api.modules.activity.pojo.form.FormPojo;
import net.xfunction.java.api.modules.activity.pojo.form.FormQuery;
import net.xfunction.java.api.modules.activity.pojo.myFavi.MySponsorListQuery;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorQuery;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorVo;
import net.xfunction.java.api.modules.activity.pojo.ticket.TicketPojo;
import net.xfunction.java.api.modules.activity.pojo.ticket.TicketQuery;
import net.xfunction.java.api.modules.activity.service.BaseService;
import net.xfunction.java.api.modules.shortlink.controller.ShortLinkController;
import net.xfunction.java.api.modules.user.query.UserQuery;
import net.xfunction.java.api.modules.user.service.SlideService;
import net.xfunction.java.api.modules.user.service.TokenService;

/**
 * 处理 我的相关，活动关注 ，活动主办方，活动表单，活动票种
 * 1，xfu_user_id 作为 必要的参数，
 * 2、多余参数置空，
 * 3、合法性较验，
 * 以上三点 防止篡改
 * @author Kelvin
 *
 */

@RestController
@Slf4j
@RequestMapping("/activity/base")
public class BaseController {

	@Resource
	private TokenService tokenService;	
	@Resource 
	private SlideService slideService;
	
	@Resource BaseService baseService;
	
	@PostMapping("/addUpdateMyForm")
	@UserLoginToken
	public Result addUpdateMyForm(@RequestBody FormPojo form , HttpServletRequest httpServletRequest) {
		log.info(form.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		form.setUserId(userIdLong);
		return baseService.createUpdateForm(form.convert());		
	}
	
	@PostMapping("/listMyForm")
	@UserLoginToken
	public Result listMyForm(@RequestBody FormQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(baseService.selectForms(query));
	}
	
	@PostMapping("/getMyForm")
	@UserLoginToken
	public Result getMyForm(@RequestBody FormQuery query,HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(query.getFormId())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);

		return Result.success(baseService.selectForm(query));
	}
	
	@PostMapping("/addUpdateMyTicket")
	@UserLoginToken
	public Result addUpdateTicket(@RequestBody TicketPojo ticket,HttpServletRequest httpServletRequest) {
		log.info(ticket.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(ticket.getActivityId())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		ticket.setUserId(userIdLong); // 冗余字段，管理功能必要，防止被篡改。
		return baseService.createUpdateTicket(ticket);	
	}
	
	@PostMapping("/listMyTicket")
	@UserLoginToken
	public Result listTicket(@RequestBody TicketQuery query,HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(query.getActivityId())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);  // 冗余字段，管理功能必要，防止被篡改。
		return Result.success(baseService.selectTickets(query));	
	}
	
	
	// 坚持不把数据结构等曝露在外面
	
	@PostMapping("getSponsor")
	@UserLoginToken
	public Result getSponsor(HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		SponsorQuery query = new SponsorQuery();
		query.setSponsorId(userIdLong);
		return Result.success(baseService.getSponsor(query));
	}
	
	@PostMapping("setSponsor")
	@UserLoginToken
	public Result setSponsor(@RequestBody SponsorVo sponsorVo , HttpServletRequest httpServletRequest) {
		log.info(sponsorVo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		sponsorVo.setSponsorId(userIdLong);
		baseService.addUpdateSponsor(sponsorVo.convertToXfuActivitySponsor());
		return Result.success();
	}

	@PostMapping("/picSponsor")
	@UserLoginToken
	public Result uploadPic(SponsorVo sponsorVo,HttpServletRequest httpServletRequest) throws IllegalStateException, IOException {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);	
		if(BaseUtils.isNull(sponsorVo.getFile())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		sponsorVo.setSponsorId(userIdLong);
		return Result.success(baseService.saveSponsorPic(sponsorVo));
	}
	
	@PostMapping("/myFaviList")
	@UserLoginToken
	public Result myFaviList(@RequestBody MySponsorListQuery query,HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if(BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(baseService.findMyFaviSponsors(query));
	}
	
	@PostMapping("/toggleFavi")
	@UserLoginToken
	public Result toggleFavi(@RequestBody MySponsorListQuery query,HttpServletRequest httpServletRequest) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		query.setUserId(userIdLong);
		if(BaseUtils.isNull(userIdLong)
				|| BaseUtils.isNull(query.getSponsorId())) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		return Result.success(baseService.ReFaviSponsor(query));
	}
	
	
}
