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
@Table(name = "xfunction.xfu_meeting_admin")
public class XfuMeetingAdmin {
    @Id
    @Column(name = "xfu_meeting_admin_id")
    private Long xfuMeetingAdminId;

    @Column(name = "xfu_admin_account")
    private String xfuAdminAccount;

    @Column(name = "xfu_admin_password")
    private String xfuAdminPassword;

    /**
     * 状态  1 有效  0 冻结
     */
    @Column(name = "xfu_admin_status")
    private Boolean xfuAdminStatus;

    /**
     * 创建时间
     */
    @Column(name = "xfu_admin_create")
    private Date xfuAdminCreate;

    /**
     * displayName
     */
    @Column(name = "xfu_admin_name")
    private String xfuAdminName;

    public static final String XFU_MEETING_ADMIN_ID = "xfuMeetingAdminId";

    public static final String XFU_ADMIN_ACCOUNT = "xfuAdminAccount";

    public static final String XFU_ADMIN_PASSWORD = "xfuAdminPassword";

    public static final String XFU_ADMIN_STATUS = "xfuAdminStatus";

    public static final String XFU_ADMIN_CREATE = "xfuAdminCreate";

    public static final String XFU_ADMIN_NAME = "xfuAdminName";
}