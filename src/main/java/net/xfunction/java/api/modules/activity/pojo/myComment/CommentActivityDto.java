package net.xfunction.java.api.modules.activity.pojo.myComment;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;

@Data
public class CommentActivityDto {

	private Long activityId;
	private String activityTitle;
	
	CommentActivityDto(XfuActivity activity){
		if(BaseUtils.isNotNull(activity)) {
			this.activityId = activity.getXfuActivityId();
			this.activityTitle = activity.getXfuActivityTitle();
		}
	}
	CommentActivityDto(){}
}
