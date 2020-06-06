package net.xfunction.java.api.modules.activity.mapper.xfunction;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;

public interface XfuActivityFormMapper extends XfunMapper<XfuActivityForm> {
	
	XfuActivityForm selectByFormId(Long xfuFormId);
}

