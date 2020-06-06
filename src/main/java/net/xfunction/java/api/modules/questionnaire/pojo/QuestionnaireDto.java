package net.xfunction.java.api.modules.questionnaire.pojo;

import java.util.Date;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireForm;

@Data
public class QuestionnaireDto {

	// private Long userId;
	private Long questionnaireId;
	private String questionnaireName;
	private String questionnaireJson;
	private Date questionnaireCreate;
	private String questionnairePre;
	private String questionnaireAfter;
	private Integer questionnaireCount;
	private Date questionnaireExpired;
	private String questionnairePic;
	
	
	public QuestionnaireDto(XfuQuestionnaireForm form){
		if(BaseUtils.isNotNull(form)) {
			this.questionnaireId = form.getXfuQuestionnaireId();
			this.questionnaireName = form.getXfuQuestionnaireName();
			this.questionnaireJson = form.getXfuQuestionnaireJson();
			this.questionnaireAfter = form.getXfuQuestionnaireAfter();
			this.questionnairePre = form.getXfuQuestionnairePre();
			this.questionnaireCreate = form.getXfuQuestionnaireCreate();
			this.questionnaireCount = form.getXfuQuestionnaireCount();
			this.questionnaireExpired = form.getXfuQuestionnaireExpired();
			this.questionnairePic = form.getXfuQuestionnairePic();
		}
	}
	
	public QuestionnaireDto() {}
}
