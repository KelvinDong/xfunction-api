package net.xfunction.java.api.modules.activity.model.xfunction;

import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = "xfu_activity_comment")
public class XfuActivityComment {
    @Id
    @Column(name = "xfu_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuCommentId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;

    @Column(name = "xfu_activity_id")
    private Long xfuActivityId;

    @Column(name = "xfu_comment_content")
    private String xfuCommentContent;

    @Column(name = "xfu_comment_del")
    private Boolean xfuCommentDel;

    @Column(name = "xfu_comment_create")
    private Date xfuCommentCreate;

    @Column(name = "xfu_comment_reply")
    private String xfuCommentReply;
    
    @Transient
    private XfuActivity activity;
    
    @Transient 
    private String xfuUserAvatar;

    /**
     * 即回复时间
     */
    @Column(name = "xfu_comment_update")
    private Date xfuCommentUpdate;

    public static final String XFU_COMMENT_ID = "xfuCommentId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_ACTIVITY_ID = "xfuActivityId";

    public static final String XFU_COMMENT_CONTENT = "xfuCommentContent";

    public static final String XFU_COMMENT_DEL = "xfuCommentDel";

    public static final String XFU_COMMENT_CREATE = "xfuCommentCreate";

    public static final String XFU_COMMENT_REPLY = "xfuCommentReply";

    public static final String XFU_COMMENT_UPDATE = "xfuCommentUpdate";
}