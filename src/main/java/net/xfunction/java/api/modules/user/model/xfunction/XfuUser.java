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
@Table(name = "xfu_user")
public class XfuUser {
    @Id
    @Column(name = "xfu_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuUserId;

    /**
     * 用户名 不区分大小写  6至20位，以字母开头，字母，数字，减号，下划线
     */
    @Column(name = "xfu_user_name")
    private String xfuUserName;
    
    @Column( name = "xfu_user_avatar")
    private String xfuUserAvatar;

    /**
     * 用户手机号
     */
    @Column(name = "xfu_user_mobile")
    private String xfuUserMobile;

    /**
     * 用户邮箱地址   
     */
    @Column(name = "xfu_user_mail")
    private String xfuUserMail;

    /**
     * 用户经加密的密码
最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符
     */
    @Column(name = "xfu_user_auth")
    private String xfuUserAuth;

    /**
     * 加密盐
     */
    @Column(name = "xfu_user_auth_salt")
    private String xfuUserAuthSalt;

    /**
     * 用户冻结位  默认0 未冻结
     */
    @Column(name = "xfu_user_blocked")
    private Boolean xfuUserBlocked;

    /**
     * 用户注册时间
     */
    @Column(name = "xfu_user_create_date")
    private Date xfuUserCreateDate;

    @Column(name = "xfu_user_update_date")
    private Date xfuUserUpdateDate;

    /**
     * 用户最近一次密码登录时间
     */
    @Column(name = "xfu_user_login_date")
    private Date xfuUserLoginDate;

    @Column(name = "xfu_real_name")
    private String xfuRealName;

    @Column(name = "xfu_real_sex")
    private Integer xfuRealSex;

    @Column(name = "xfu_real_birth")
    private Date xfuRealBirth;

    @Column(name = "xfu_real_position")
    private String xfuRealPosition;

    /**
     * 所在组织
     */
    @Column(name = "xfu_real_orga")
    private String xfuRealOrga;

    @Column(name = "xfu_real_mobile")
    private String xfuRealMobile;

    @Column(name = "xfu_real_email")
    private String xfuRealEmail;
    
    @Column(name = "xfu_tool_settings")
    private String xfuToolSettings;
    

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_USER_NAME = "xfuUserName";

    public static final String XFU_USER_MOBILE = "xfuUserMobile";

    public static final String XFU_USER_MAIL = "xfuUserMail";

    public static final String XFU_USER_AUTH = "xfuUserAuth";

    public static final String XFU_USER_AUTH_SALT = "xfuUserAuthSalt";

    public static final String XFU_USER_BLOCKED = "xfuUserBlocked";

    public static final String XFU_USER_CREATE_DATE = "xfuUserCreateDate";

    public static final String XFU_USER_UPDATE_DATE = "xfuUserUpdateDate";

    public static final String XFU_USER_LOGIN_DATE = "xfuUserLoginDate";

    public static final String XFU_REAL_NAME = "xfuRealName";

    public static final String XFU_REAL_SEX = "xfuRealSex";

    public static final String XFU_REAL_BIRTH = "xfuRealBirth";

    public static final String XFU_REAL_POSITION = "xfuRealPosition";

    public static final String XFU_REAL_ORGA = "xfuRealOrga";

    public static final String XFU_REAL_MOBILE = "xfuRealMobile";

    public static final String XFU_REAL_EMAIL = "xfuRealEmail";
}