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
@Table(name = "xfu_activity_sponsor")
public class XfuActivitySponsor {
    @Id
    @Column(name = "xfu_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuUserId;

    @Column(name = "xfu_sponsor_name")
    private String xfuSponsorName;

    @Column(name = "xfu_sponsor_logo")
    private String xfuSponsorLogo;

    @Column(name = "xfu_sponsor_intro")
    private String xfuSponsorIntro;

    @Column(name = "xfu_sponsor_create")
    private Date xfuSponsorCreate;

    @Column(name = "xfu_sponsor_update")
    private Date xfuSponsorUpdate;

    public static final String XFU_SPONSOR_ID = "xfuSponsorId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_SPONSOR_NAME = "xfuSponsorName";

    public static final String XFU_SPONSOR_LOGO = "xfuSponsorLogo";

    public static final String XFU_SPONSOR_INTRO = "xfuSponsorIntro";

    public static final String XFU_SPONSOR_CREATE = "xfuSponsorCreate";

    public static final String XFU_SPONSOR_UPDATE = "xfuSponsorUpdate";
}