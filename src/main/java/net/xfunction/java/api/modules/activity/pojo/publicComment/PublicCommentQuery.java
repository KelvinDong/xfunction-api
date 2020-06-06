package net.xfunction.java.api.modules.activity.pojo.publicComment;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;

@Data
public class PublicCommentQuery extends BaseQuery<XfuActivityComment> {
	
	private Long activityId;

}
