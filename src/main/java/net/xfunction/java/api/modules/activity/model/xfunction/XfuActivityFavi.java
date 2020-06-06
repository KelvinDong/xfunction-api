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
@Table(name = "xfu_activity_favi")
public class XfuActivityFavi {
    @Id
    @Column(name = "xfu_favi_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuFaviId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;

    @Column(name = "xfu_sponsor_id")
    private Long xfuSonsorId;

    /**
     * 关注1，0 不再关注了
     */
    @Column(name = "xfu_favi_status")
    private Boolean xfuFaviStatus;

    @Column(name = "xfu_favi_create")
    private Date xfuFaviCreate;

    @Column(name = "xfu_favi_update")
    private Date xfuFaviUpdate;

    public static final String XFU_FAVI_ID = "xfuFaviId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_ACTIVITY_ID = "xfuActivityId";

    public static final String XFU_FAVI_STATUS = "xfuFaviStatus";

    public static final String XFU_FAVI_CREATE = "xfuFaviCreate";

    public static final String XFU_FAVI_UPDATE = "xfuFaviUpdate";
}