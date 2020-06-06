package net.xfunction.java.api.modules.activity.service;

import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityDto;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivityList.PublicActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.publicComment.PublicCommentQuery;

public interface PublicService {




	PublicActivityDto getPublicActity(PublicActivityQuery query);

	PageResultSet getPublicActityList(PublicActivityListQuery query);

	PageResultSet getSponsorActityList(PublicActivityListQuery query);

	PageResultSet getPublicComments(PublicCommentQuery query);



}
