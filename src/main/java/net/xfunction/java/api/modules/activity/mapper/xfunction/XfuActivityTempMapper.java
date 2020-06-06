package net.xfunction.java.api.modules.activity.mapper.xfunction;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;

public interface XfuActivityTempMapper extends XfunMapper<XfuActivityTemp> {
	
	XfuActivityTemp selectActivityTempForAsso(Long xfuActivityId);
}