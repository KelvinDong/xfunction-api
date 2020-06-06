package net.xfunction.java.api.modules.activity.pojo.activityComment;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;

@Data
public class ActivityCommentQuery extends BaseQuery<XfuActivityComment>{
	private Long UserId;
	private Long commentId;
}
