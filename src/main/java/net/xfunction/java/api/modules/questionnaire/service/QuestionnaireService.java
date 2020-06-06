package net.xfunction.java.api.modules.questionnaire.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.questionnaire.pojo.PicFileDto;
import net.xfunction.java.api.modules.questionnaire.pojo.PicFileQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireEntryQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireFormVo;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireMyListQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireMyQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnairePublicQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnairePublicVo;

public interface QuestionnaireService {


	Result createUpdateQuestionnaireForm(QuestionnaireFormVo vo);

	PicFileDto savePic(PicFileQuery query) throws IllegalStateException, IOException;

	Result getMyQuestionnaire(QuestionnaireMyQuery query);

	Result getMyQuestionnaires(QuestionnaireMyListQuery query);

	Result gePublicQuestionnaire(QuestionnairePublicQuery query);

	Result addQuestionnaireEntry(QuestionnairePublicVo vo);

	Result getQuestionnaireEntries(QuestionnaireEntryQuery query);

	void dlQuestionnaireEntries(QuestionnaireEntryQuery query, HttpServletResponse response);

}
