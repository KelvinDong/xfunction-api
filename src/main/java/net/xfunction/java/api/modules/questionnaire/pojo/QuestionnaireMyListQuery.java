package net.xfunction.java.api.modules.questionnaire.pojo;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireForm;

@Data
public class QuestionnaireMyListQuery extends BaseQuery<XfuQuestionnaireForm>  {
	private Long userId;
	private Boolean showExpired = true;
	private Integer questionnaireType=1;
}
