package net.xfunction.java.api.modules.activity.mapper.xfunction;

import java.util.List;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;

public interface XfuActivitySponsorMapper extends XfunMapper<XfuActivitySponsor> {
	
	XfuActivitySponsor selectByUserId(Long xfuUserId);

	List<XfuActivitySponsor> selectByFavi(Long userId);
	
	
}