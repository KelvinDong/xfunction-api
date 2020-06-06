package net.xfunction.java.api.modules.activity.pojo.myComment;



import java.util.Date;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;
@Data
public class CommentDto {

	private Long commentId;
	private String commentContent;
	private String commentReply;
	private Date commentCreate;
	private Date commentUpdate;
	private Boolean commentDel;
	private CommentActivityDto activity;
	private String userAvatar;
	
	public CommentDto(XfuActivityComment comment){
		if(BaseUtils.isNotNull(comment)) {
			this.commentId = comment.getXfuCommentId();
			this.commentContent = comment.getXfuCommentContent();
			this.commentReply = comment.getXfuCommentReply();
			this.commentDel = comment.getXfuCommentDel();
			this.commentCreate = comment.getXfuCommentCreate();
			this.commentUpdate = comment.getXfuCommentUpdate();
			this.userAvatar = comment.getXfuUserAvatar();
			this.activity = new CommentActivityDto(comment.getActivity());
		}
	}
	
	public CommentDto(){}
	
	
}
