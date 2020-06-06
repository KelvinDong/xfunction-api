package net.xfunction.java.api.modules.activity.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.socket.MyHandler;
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
import net.xfunction.java.api.modules.activity.pojo.myActivityWall.MyActivityWallQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityWall.MyActivityWallVo;
import net.xfunction.java.api.modules.activity.pojo.myComment.MyCommentsQuery;
import net.xfunction.java.api.modules.activity.pojo.myComment.MyCommnetVo;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryVo;
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
@RequestMapping("/activity/main")
public class ActivityController {

	@Resource
	private TokenService tokenService;
	@Resource
	private SlideService slideService;
	@Resource
	private MainService mainService;
	
	

	@PostMapping("/applyMyActivity")
	@UserLoginToken
	public Result applyMyActivity(@RequestBody MyActivityListQuery query, HttpServletRequest httpServletRequest) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		switch (query.getMyApplyId()) {
		case 2: // 提审
			if (!mainService.updateActivityByUp(query)) {
				return Result.failure(ResultCodeEnum.PARAM_ERROR, "编辑状态、内容完整且未过期才能提审");
			}
			break;
		case 3: // 下架
			return mainService.updateActivityByDown(query);
			//break;
		default:
			return Result.failure(ResultCodeEnum.PARAM_ERROR);
		// break;
		}
		return Result.success();
	}
	
	@PostMapping("/saveLotterySettings")
	@UserLoginToken
	public Result saveLotterySettings(@RequestBody MyActivityLotteryVo vo, HttpServletRequest httpServletRequest) {
		log.info(vo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(vo.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.saveActivityLotterySettings(vo);
	}
	
	@PostMapping("/getLotterySettings")
	@UserLoginToken
	public Result getLotterySettings(@RequestBody MyActivityLotteryQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(mainService.getActivityLotterySettings(query));
	}
	
	@PostMapping("/getLotteryEntries")
	@UserLoginToken
	public Result getLotteryEntries(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest) {
		// log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return (mainService.getMyActivityAllEntries(query));
	}
	
	@PostMapping("/saveLotteryResult")
	@UserLoginToken
	public Result saveLotteryResult(@RequestBody LotteryResultVo vo ,HttpServletRequest httpServletRequest) {
		log.info(vo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(vo.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.addActivityLotteryResult(vo);
	}
	
	@PostMapping("/getLotteryResults")
	@UserLoginToken
	public Result getLotteryResults(@RequestBody LotteryQuery query,HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(mainService.getActivityLotteryResults(query));
	}
	
	@PostMapping("/dlLotteryResults")
	@UserLoginToken
	public void dlLotteryResults(@RequestBody LotteryQuery query,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return ;
		query.setUserId(userIdLong);
		mainService.dlActivityLotteryResults(query,httpServletResponse);
	}
	
	@PostMapping("/listMyActivity")
	@UserLoginToken
	public Result listMyActivity(@RequestBody MyActivityListQuery query, HttpServletRequest httpServletRequest) {

		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);

		query.setUserId(userIdLong);
		return Result.success(mainService.getMyActivityList(query));
	}

	@PostMapping("/addUpdateMyActivity")
	@UserLoginToken
	public Result addUpdateMyActivity(@RequestBody MyActivityTempPojo vo, HttpServletRequest httpServletRequest) {
		log.info(vo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.createUpdateActivity(vo.convertToXfuActivityTemp());
	}

	@PostMapping("/uploadPic")
	@UserLoginToken
	public Result uploadPic(PicFileQuery query, HttpServletRequest httpServletRequest)
			throws IllegalStateException, IOException {
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(query.getFile()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		// Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		return Result.success(mainService.savePic(query));
	}

	@PostMapping("/getMyActivityTemp")
	@UserLoginToken
	public Result getMyActivityTemp(@RequestBody MyActivityTempQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);

		query.setUserId(userIdLong);
		return Result.success(mainService.selectActivityTemp(query));
	}

	@PostMapping("/addMyEntry")
	@UserLoginToken
	public Result addMyEntry(@RequestBody MyEntryVo entry, HttpServletRequest httpServletRequest) {
		log.info(entry.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(entry.getActivityId()) || BaseUtils.isNull(entry.getTicketId())
				|| BaseUtils.isNull(userIdLong) || BaseUtils.isEmpty(entry.getEntryContent()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);

		entry.setUserId(userIdLong);
		return mainService.addEntry(entry.convert());
	}
	
	@PostMapping("/importEntry")
	@UserLoginToken
	public Result importEntry( MyEntryVo entry, HttpServletRequest httpServletRequest) throws IllegalStateException, IOException {
		log.info(entry.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(entry.getActivityId()) 
				|| BaseUtils.isNull(userIdLong) || BaseUtils.isNull(entry.getFile()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);

		entry.setUserId(userIdLong);
		return mainService.importActivityEntries(entry);
	}
	

	@PostMapping("/cancelMyEntry")
	@UserLoginToken
	public Result cancelMyEntry(@RequestBody MyEntryQuery query, HttpServletRequest httpServletRequest) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getEntryId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return mainService.cancelEntry(query);

	}

	@PostMapping("/listMyEntry")
	@UserLoginToken
	public Result listMyEntry(@RequestBody MyEntryQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(mainService.getMyEntries(query));
	}

	@PostMapping("/listMyActivityEntries")
	@UserLoginToken
	public Result listMyActivityEntries(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return (mainService.getMyActivityEntries(query));
	}

	@PostMapping("/dlMyActivityEntries")
	@UserLoginToken
	public void dlMyActivityEntries(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest,
			HttpServletResponse response) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return;
		query.setUserId(userIdLong);
		mainService.dlMyActivityEntries(query, response);
	}
	
	@PostMapping("/dlMyActivityEntriesTemple")
	@UserLoginToken
	public void dlMyActivityEntriesTemple(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest,
			HttpServletResponse response) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getActivityId()) || BaseUtils.isNull(userIdLong))
			return;
		query.setUserId(userIdLong);
		mainService.dlMyActivityEntriesTemple(query, response);
	}

	// 出示个人签到码
	@PostMapping("/signPerson")
	@UserLoginToken
	public Result signPerson(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getId()) || BaseUtils.isNull(query.getCode()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);

		query.setUserId(userIdLong);
		return mainService.signByEntryCheckinCode(query);
	}

	// 扫描活动现场表
	@PostMapping("/signActivity")
	@UserLoginToken
	public Result signActivity(@RequestBody MyEntryQuery query, HttpServletRequest httpServletRequest) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getId()) || BaseUtils.isNull(query.getCode()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);

		query.setUserId(userIdLong);
		return mainService.singByActivityToken(query);
	}

	// 由主办方代为签到
	@PostMapping("/sign")
	@UserLoginToken
	public Result sign(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getEntryId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return mainService.signBySponsor(query);
	}
	
	// 由主办方代为取消，恢复
		@PostMapping("/cancel")
		@UserLoginToken
		public Result cancel(@RequestBody ActivityEntryQuery query, HttpServletRequest httpServletRequest) {
			log.info(query.toString());
			Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
			if (BaseUtils.isNull(query.getEntryId()) || BaseUtils.isNull(userIdLong))
				return Result.failure(ResultCodeEnum.PARAMS_MISS);
			query.setUserId(userIdLong);
			return mainService.cancelBySponsor(query);
		}
	
	@PostMapping("/publishComment")
	@UserLoginToken
	public Result publishComment(@RequestBody MyCommnetVo vo, HttpServletRequest httpServletRequest) {
		log.info(vo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(vo.getActivityId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.createComment(vo.convert());
		 
	}
	
	//用于回复，和删除申诉
	@PostMapping("/applyComment")
	@UserLoginToken
	public Result applyComment(@RequestBody ActivityCommentVo vo, HttpServletRequest httpServletRequest) {
		log.info(vo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(vo.getCommentId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.replayComment(vo);
	}
	
	@PostMapping("/delMyComment")
	@UserLoginToken
	public Result delMyComment(@RequestBody MyCommentsQuery query, HttpServletRequest httpServletRequest) {
		log.info(query.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getCommentId()) || BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return mainService.delComment(query);
	}
	
	@PostMapping("/listMyComment")
	@UserLoginToken
	public Result listMyComment(@RequestBody MyCommentsQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if ( BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(mainService.getMyComments(query));
	}
	
	@PostMapping("/listActivityComment")
	@UserLoginToken
	public Result listActivityComment(@RequestBody ActivityCommentQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if ( BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return Result.success(mainService.getActivityComments(query));
	}
	
	/**
	 *  暂未使用
	 * @param vo
	 * @param httpServletRequest
	 * @return
	 */
	@PostMapping("/setWallSetting")
	@UserLoginToken
	public Result setWallSetting(@RequestBody MyActivityWallVo vo ,HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if ( BaseUtils.isNull(vo.getActivityId()) ||  BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return mainService.saveWallSettings(vo);
	}
	
	@PostMapping("/getWallSetting") //包括所有报名总数，包括取消的，和已经签到的。
	@UserLoginToken
	public Result getWallSetting(@RequestBody MyActivityWallQuery query ,HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if ( BaseUtils.isNull(query.getActivityId()) ||  BaseUtils.isNull(userIdLong))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return mainService.getActivityWallSettingsAndTotal(query);
	}
	

}
