package net.xfunction.java.api.modules.activity.mapper.xfunction;

import java.util.List;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;
import net.xfunction.java.api.modules.activity.pojo.activityEntry.ActivityEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryQuery;

public interface XfuActivityEntryMapper extends XfunMapper<XfuActivityEntry> {
	
	List<XfuActivityEntry> selectMyEntries(MyEntryQuery query);
	List<XfuActivityEntry> selectActivityEntries(ActivityEntryQuery query);
	
}