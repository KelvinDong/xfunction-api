package net.xfunction.java.api.modules.activity.pojo.form;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;

@Data
public class FormQuery extends BaseQuery<XfuActivityForm>{   

	private Long userId;
	private Long formId;
}
