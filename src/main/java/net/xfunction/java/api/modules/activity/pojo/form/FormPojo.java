package net.xfunction.java.api.modules.activity.pojo.form;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;

@Data
public class FormPojo {
	private Long formId;
	private Long userId;
	private String formName;
	private String formJson;
	
	public FormPojo(XfuActivityForm form) {   // Dto
		if(BaseUtils.isNotNull(form)) {
		this.formId = form.getXfuFormId();
		this.formName = form.getXfuFormName();
		this.formJson = form.getXfuFormJson();
		}
	}
	
	public FormPojo() {
		
	}
	
	public XfuActivityForm convert() {	// Vo
		XfuActivityForm form = new XfuActivityForm();
		form.setXfuUserId(this.userId);
		form.setXfuFormId(this.formId);
		form.setXfuFormName(this.formName);
		form.setXfuFormJson(this.formJson);
		return form;
	}
}
