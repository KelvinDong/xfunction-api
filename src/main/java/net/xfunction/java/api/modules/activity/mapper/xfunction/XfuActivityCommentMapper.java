package net.xfunction.java.api.modules.activity.mapper.xfunction;

import java.util.List;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentQuery;
import net.xfunction.java.api.modules.activity.pojo.publicComment.PublicCommentQuery;

public interface XfuActivityCommentMapper extends XfunMapper<XfuActivityComment> {
	
	List<XfuActivityComment> selectMyComments(Long userId);
	
	List<XfuActivityComment> selectActivityComments(ActivityCommentQuery query);

	List<XfuActivityComment> selectPublicComments(Long activityId);
	
}