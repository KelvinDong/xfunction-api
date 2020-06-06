package net.xfunction.java.api.modules.activity.pojo.myComment;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;

@Data
public class MyCommentsQuery extends BaseQuery<XfuActivityComment>{
	private Long userId;
	
	// del
	private Long commentId;
}
