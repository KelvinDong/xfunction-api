package net.xfunction.java.api.modules.activity.pojo.activityComment;

import lombok.Data;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;

@Data
public class ActivityCommentVo {

	private Long userId; // 用于判断对应的活动是不是我的
	private Long CommentId;
	private String commentReply;
	private Boolean commentDel;
	
	
	public XfuActivityComment convert() {
		XfuActivityComment comment = new XfuActivityComment();
		comment.setXfuCommentId(this.CommentId);
		comment.setXfuCommentDel(this.commentDel);
		comment.setXfuCommentReply(this.commentReply);
		return comment;
	}
}
