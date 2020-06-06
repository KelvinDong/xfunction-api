package net.xfunction.java.api.modules.activity.mapper.xfunction;

import java.util.List;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivityList.PublicActivityListQuery;

public interface XfuActivityMapper extends XfunMapper<XfuActivity> {

	List<XfuActivity> selectMyActivityList(MyActivityListQuery query);
	
	List<XfuActivity> selectPublicActivityList(PublicActivityListQuery query);
	
	List<XfuActivity> selectSponsorActivityList(PublicActivityListQuery query);
	
	XfuActivity selectPublicActivity(PublicActivityQuery query);
	
	XfuActivity selectMyActivity(Long activityId);

	void updateActivityVisit (PublicActivityQuery query);
	XfuActivity selectActivityForAsso(Long xfuActivityId);
}