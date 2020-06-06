package net.xfunction.java.api.modules.questionnaire.pojo;

import java.util.Date;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireEntry;

@Data
public class QuestionnaireEntryDto {
	
	private String entryContent;
	private Date entryCreate;
	
	public QuestionnaireEntryDto(XfuQuestionnaireEntry entry) {
		if(BaseUtils.isNotNull(entry)) {
			this.entryContent = entry.getXfuEntryContent();
			this.entryCreate = entry.getXfuEntryCreate();
		}
	}

}
