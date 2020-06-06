package net.xfunction.java.api.modules.questionnaire.pojo;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireEntry;

@Data
public class QuestionnaireEntryQuery extends BaseQuery<XfuQuestionnaireEntry> {

	private Long questionnaireId;
	private String queryStr;
	private Long userId;
	
}
