package net.xfunction.java.api.modules.activity.pojo.form;

import java.util.Date;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;

@Data
public class FormListDto {

	private Long formId;
	private String formName;
	private Date formUpdate;
	
	public FormListDto(XfuActivityForm form) {
		if(BaseUtils.isNotNull(form)) {
		this.formId = form.getXfuFormId();
		this.formName = form.getXfuFormName();
		this.formUpdate = form.getXfuFormUpdate();
		}
	}
	public FormListDto() {}
	
}
