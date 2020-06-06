package net.xfunction.java.api.modules.activity.pojo.publicActivity;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;

@Data
public class PublicActivityFormDto {

	private String formJson;
	
	public PublicActivityFormDto(XfuActivityForm form) {
		if(BaseUtils.isNotNull(form)) {
		this.formJson = form.getXfuFormJson();
		}
	}
	
	public PublicActivityFormDto() {}
	
}
