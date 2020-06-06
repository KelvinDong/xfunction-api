package net.xfunction.java.api.modules.questionnaire.pojo;

import lombok.Data;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireEntry;

@Data
public class QuestionnairePublicVo {
	
	private Long questionnaireId;
	private String entryContent;
	private Long userId;
	private Integer move;
	private Integer action[];
	
	
	public XfuQuestionnaireEntry convert() {
		XfuQuestionnaireEntry entry = new XfuQuestionnaireEntry();
		entry.setXfuEntryContent(this.entryContent);
		entry.setXfuQuestionnaireId(this.questionnaireId);
		entry.setXfuUserId(this.userId);
		return entry;
	}

}
