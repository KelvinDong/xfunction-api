package net.xfunction.java.api.modules.activity.pojo.myComment;

import lombok.Data;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;

@Data
public class MyCommnetVo {
	private Long userId;
	private Long activityId;
	private String commentContent;
	
	public XfuActivityComment convert() {
		XfuActivityComment comment = new XfuActivityComment();
		comment.setXfuActivityId(this.activityId);
		comment.setXfuUserId(this.userId);
		comment.setXfuCommentContent(this.commentContent);
		return comment;
	}
}

