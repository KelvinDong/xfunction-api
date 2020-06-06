package net.xfunction.java.api.modules.questionnaire.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireForm;

@Data
public class QuestionnaireFormVo {
	private Long userId;
	private Integer questionnaireType =1;
	private Long questionnaireId;
	private String questionnaireName;
	private String questionnaireJson;
	private String questionnairePre;
	private String questionnaireAfter;
	private String questionnairePic;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date questionnarieExpired;
	private Long fileId;
	
	public XfuQuestionnaireForm convert() {
		XfuQuestionnaireForm form = new XfuQuestionnaireForm();
		form.setXfuUserId(this.userId);
		form.setXfuQuestionnaireType(this.questionnaireType);
		form.setXfuQuestionnaireId(this.questionnaireId);
		form.setXfuQuestionnaireName(this.questionnaireName);
		form.setXfuQuestionnaireJson(this.questionnaireJson);
		form.setXfuQuestionnairePre(this.questionnairePre);
		form.setXfuQuestionnaireAfter(this.questionnaireAfter);
		form.setXfuQuestionnairePic(this.questionnairePic);
		form.setXfuQuestionnaireExpired(this.questionnarieExpired);
		return form;
	}
	
}
