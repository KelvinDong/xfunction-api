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
@Table(name = "xfu_activity_form")
public class XfuActivityForm {
    @Id
    @Column(name = "xfu_form_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuFormId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;

    @Column(name = "xfu_form_name")
    private String xfuFormName;

    @Column(name = "xfu_form_create")
    private Date xfuFormCreate;

    @Column(name = "xfu_form_update")
    private Date xfuFormUpdate;

    @Column(name = "xfu_form_json")
    private String xfuFormJson;

    public static final String XFU_FORM_ID = "xfuFormId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_FORM_NAME = "xfuFormName";

    public static final String XFU_FORM_CREATE = "xfuFormCreate";

    public static final String XFU_FORM_UPDATE = "xfuFormUpdate";

    public static final String XFU_FORM_JSON = "xfuFormJson";
}