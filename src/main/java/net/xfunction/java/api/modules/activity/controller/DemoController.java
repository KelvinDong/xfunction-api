package net.xfunction.java.api.modules.activity.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;
import net.xfunction.java.api.modules.activity.pojo.PicFileQuery;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentQuery;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentVo;
import net.xfunction.java.api.modules.activity.pojo.activityEntry.ActivityEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryResultVo;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryVo;
import net.xfunction.java.api.modules.activity.pojo.myActivityTemp.MyActivityTempPojo;
import net.xfunction.java.api.modules.activity.pojo.myActivityTemp.MyActivityTempQuery;
import net.xfunction.java.api.modules.activity.pojo.myComment.MyCommentsQuery;
import net.xfunction.java.api.modules.activity.pojo.myComment.MyCommnetVo;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryVo;
import net.xfunction.java.api.modules.activity.pojo.ticket.TicketQuery;
import net.xfunction.java.api.modules.activity.service.BaseService;
import net.xfunction.java.api.modules.activity.service.MainService;
import net.xfunction.java.api.modules.user.query.UserQuery;
import net.xfunction.java.api.modules.user.service.SlideService;
import net.xfunction.java.api.modules.user.service.TokenService;

/**
 * @author Kelvin
 *
 */

@RestController
@Slf4j
@RequestMapping("/activity/demo")
public class DemoController {

	@Resource
	private TokenService tokenService;
	@Resource
	private SlideService slideService;
	@Resource
	private MainService mainService;
	@Resource BaseService baseService;

	
	@PostMapping("/saveLotterySettings")
	public Result saveLotterySettings(@RequestBody MyActivityLotteryVo vo, HttpServletRequest httpServletRequest) {
		Long userIdLong = 1l;
		vo.setActivityId(1l);
		if (BaseUtils.isNull(vo.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.saveActivityLotterySettings(vo);
	}
	
	@PostMapping("/getLotterySettings")
	public Result getLotterySettings(@RequestBody MyActivityLotteryQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = 1l;
		query.setActivityId(1l);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(mainService.getActivityLotterySettings(query));
	}
	
	@PostMapping("/getLotteryEntries")
	public Result getLotteryEntries(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = 1l;
		query.setActivityId(1l);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return (mainService.getMyActivityAllEntries(query));
	}
	
	@PostMapping("/saveLotteryResult")
	public Result saveLotteryResult(@RequestBody LotteryResultVo vo ,HttpServletRequest httpServletRequest) {
		Long userIdLong = 1l;
		vo.setActivityId(1l);
		if (BaseUtils.isNull(vo.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.addActivityLotteryResult(vo);
	}
	
	@PostMapping("/getLotteryResults")
	public Result getLotteryResults(@RequestBody LotteryQuery query,HttpServletRequest httpServletRequest) {
		Long userIdLong = 1l;
		query.setActivityId(1l);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(mainService.getActivityLotteryResults(query));
	}
	
	@PostMapping("/dlLotteryResults")
	public void dlLotteryResults(@RequestBody LotteryQuery query,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Long userIdLong = 1l;
		query.setActivityId(1l);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return ;
		query.setUserId(userIdLong);
		mainService.dlActivityLotteryResults(query,httpServletResponse);
	}
	
	@PostMapping("/uploadPic")
	public Result uploadPic(PicFileQuery query, HttpServletRequest httpServletRequest)
			throws IllegalStateException, IOException {
		Long userIdLong = 1l;
		query.setActivityId(1l);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(query.getFile()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		// Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		return Result.success(mainService.savePic(query));
	}

	
	@PostMapping("/listMyTicket")
	public Result listTicket(@RequestBody TicketQuery query,HttpServletRequest httpServletRequest) {
		Long userIdLong = 1l;
		query.setActivityId(1l);
		if(BaseUtils.isNull(query.getActivityId())
				|| BaseUtils.isNull(userIdLong)) return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);  // 冗余字段，管理功能必要，防止被篡改。
		return Result.success(baseService.selectTickets(query));	
	}
	
}
