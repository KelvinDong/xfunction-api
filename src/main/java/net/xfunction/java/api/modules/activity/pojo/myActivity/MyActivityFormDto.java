package net.xfunction.java.api.modules.activity.pojo.myActivity;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;

@Data
public class MyActivityFormDto {

	private String formJson;
	
	public MyActivityFormDto(XfuActivityForm form) {
		if(BaseUtils.isNotNull(form)) {
		this.formJson = form.getXfuFormJson();
		}
	}
	public MyActivityFormDto() {}
}
