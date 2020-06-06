package net.xfunction.java.api.modules.questionnaire.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.activity.controller.ActivityController;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.PicFileQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireEntryQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireFormVo;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireMyListQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireMyQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnairePublicQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnairePublicVo;
import net.xfunction.java.api.modules.questionnaire.service.QuestionnaireService;
import net.xfunction.java.api.modules.user.query.SlideQuery;
import net.xfunction.java.api.modules.user.service.SlideService;
import net.xfunction.java.api.modules.user.service.TokenService;

@RestController
@Slf4j
@RequestMapping("/tools/main")
public class QuestionnaireController {

	@Resource
	private TokenService tokenService;
	
	@Resource SlideService slideService;
	
	@Resource
	private QuestionnaireService questionnaireService;
	
	@PostMapping("uploadPic")
	@UserLoginToken
	public Result uploadPic(PicFileQuery query, HttpServletRequest httpServletRequest) throws IllegalStateException, IOException {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong) || BaseUtils.isNull(query.getFile()))
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);  // userId 主要用于新增前 无业务ID时临时保存图片
		return Result.success(questionnaireService.savePic(query));
	}
	
	@PostMapping("setQuestionnaire")
	@UserLoginToken
	public Result setQuestionnaire(@RequestBody QuestionnaireFormVo vo, HttpServletRequest httpServletRequest) {
		log.info(vo.toString());
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong) )
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return questionnaireService.createUpdateQuestionnaireForm(vo);
	}
	
	@PostMapping("getMyQuestionnaire")
	@UserLoginToken
	public Result getMyQuestionnaire(@RequestBody QuestionnaireMyQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong) || BaseUtils.isNull(query.getQuestionnaireId()) )
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return questionnaireService.getMyQuestionnaire(query);
	}
	
	@PostMapping("listMyQuestionnaire")
	@UserLoginToken
	public Result listMyQuestionnaire(@RequestBody QuestionnaireMyListQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong) )
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return questionnaireService.getMyQuestionnaires(query);
	}
	
	
	@PostMapping("getPublicQuestionnaire")
	// @UserLoginToken
	public Result getPublicQuestionnaire(@RequestBody QuestionnairePublicQuery query, HttpServletRequest httpServletRequest) {
		// Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(query.getQuestionnaireId()) )
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		// query.setUserId(userIdLong);
		return questionnaireService.gePublicQuestionnaire(query);
	}
	
	/**
	 * 无需登录， 未来有公众号，考虑公众号授权限制一个用户只能提交一次。
	 * @param vo
	 * @param httpServletRequest
	 * @return
	 */
	@PostMapping("addQuestionnaireEntry")
	// @UserLoginToken
	public Result addQuestionnaireEntry(@RequestBody QuestionnairePublicVo vo, HttpServletRequest httpServletRequest) {
		log.info(vo.toString());
		SlideQuery slideQuery =  new SlideQuery();
		slideQuery.setAction(vo.getAction());
		slideQuery.setMove(vo.getMove());		
		if(!slideService.vertify(slideQuery, httpServletRequest)) {
			return Result.failure(ResultCodeEnum.FAILD,"拼图验证失败");
		}
		
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(vo.getQuestionnaireId()) )
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		vo.setUserId(userIdLong);
		return questionnaireService.addQuestionnaireEntry(vo);
	}
	
	@PostMapping("getQuestionnaireEntries")
	@UserLoginToken
	public Result getQuestionnaireEntries(@RequestBody QuestionnaireEntryQuery query, HttpServletRequest httpServletRequest) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong) || BaseUtils.isNull(query.getQuestionnaireId()) )
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		query.setUserId(userIdLong);
		return questionnaireService.getQuestionnaireEntries(query);
	}
	
	@PostMapping("dlQuestionnaireEntries")
	@UserLoginToken
	public void dlQuestionnaireEntries(@RequestBody QuestionnaireEntryQuery query, HttpServletRequest httpServletRequest,HttpServletResponse response) {
		Long userIdLong = tokenService.getUserIdFromRequest(httpServletRequest);
		if (BaseUtils.isNull(userIdLong) || BaseUtils.isNull(query.getQuestionnaireId()) )
			return;
		query.setUserId(userIdLong);
		questionnaireService.dlQuestionnaireEntries(query,response);
	}
}
