package net.xfunction.java.api.modules.user.model.xfunction;

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
@Table(name = "xfu_user_reset")
public class XfuUserReset {
    @Id
    @Column(name = "xfu_reset_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuResetId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;

    @Column(name = "xfu_reset_token")
    private String xfuResetToken;

    @Column(name = "xfu_reset_expired")
    private Date xfuResetExpired;

    /**
     * 是否完成重置
     */
    @Column(name = "xfu_reset_has")
    private Boolean xfuResetHas;
    
    /**
     * 邮件发送次数
     */
    @Column(name = "xfu_reset_times")
    private Integer xfuResetTimes;
    
    @Column(name = "xfu_user_mail")
    private String xfuUserMail;
    
    @Column(name = "xfu_create_date")
    private Date xfuCreateDate;
    
    @Column(name = "xfu_update_date")
    private Date xfuUpdateDate;

    public static final String XFU_RESET_ID = "xfuResetId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_RESET_TOKEN = "xfuResetToken";

    public static final String XFU_RESET_EXPIRED = "xfuResetExpired";

    public static final String XFU_RESET_HAS = "xfuResetHas";
}