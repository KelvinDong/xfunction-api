package net.xfunction.java.api.modules.meeting.model.xfunction;

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
@Table(name = "xfunction.xfu_meeting")
public class XfuMeeting {
    @Id
    @Column(name = "xfu_channel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuChannelId;

    @Column(name = "xfu_channel_name")
    private String xfuChannelName;

    /**
     * 创建时间
     */
    @Column(name = "xfu_channel_create")
    private Date xfuChannelCreate;

    /**
     * 频道状态 1 有效 2 冻结
     */
    @Column(name = "xfu_channel_status")
    private Boolean xfuChannelStatus;

    /**
     * 会议邀请码
     */
    @Column(name = "xfu_channel_code")
    private String xfuChannelCode;

    public static final String XFU_CHANNEL_ID = "xfuChannelId";

    public static final String XFU_CHANNEL_NAME = "xfuChannelName";

    public static final String XFU_CHANNEL_CREATE = "xfuChannelCreate";

    public static final String XFU_CHANNEL_STATUS = "xfuChannelStatus";

    public static final String XFU_CHANNEL_CODE = "xfuChannelCode";
}