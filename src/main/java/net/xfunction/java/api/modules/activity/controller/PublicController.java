package net.xfunction.java.api.modules.activity.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivityList.PublicActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.publicComment.PublicCommentQuery;
import net.xfunction.java.api.modules.activity.service.MainService;
import net.xfunction.java.api.modules.activity.service.PublicService;

@RestController
@Slf4j
@RequestMapping("/activity/public")
public class PublicController {

	@Resource
	private PublicService publicService;
	
	@PostMapping("/getActivity")
	public Result getActivity(@RequestBody PublicActivityQuery query,HttpServletRequest httpServletRequest){
		if(BaseUtils.isNull(query.getActivityId()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		return Result.success(publicService.getPublicActity(query));		
	}
	
	@PostMapping("/getActivityList")
	public Result getActivityList(@RequestBody PublicActivityListQuery query,HttpServletRequest httpServletRequest){
		return Result.success(publicService.getPublicActityList(query));		
	}
	
	@PostMapping("/getSponsorActivityList")
	public Result getSponsorActivityList(@RequestBody PublicActivityListQuery query,HttpServletRequest httpServletRequest){
		if(BaseUtils.isNull(query.getSponsorId()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		return Result.success(publicService.getSponsorActityList(query));		
	}
	
	@PostMapping("/getPublicComments")
	public Result getPublicComments(@RequestBody PublicCommentQuery query, HttpServletRequest httpServletRequest) {
		if(BaseUtils.isNull(query.getActivityId()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		return Result.success(publicService.getPublicComments(query));
	}
	
	
}
