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
@Table(name = "xfu_upload_img")
public class XfuUploadImg {
    @Id
    @Column(name = "xfu_upload_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuUploadId;

    @Column(name = "xfu_upload_src")
    private String xfuUploadSrc;

    /**
     * 类型，为了对应的功能  1 活动
     */
    @Column(name = "xfu_upload_fun_type")
    private Integer xfuUploadFunType;

    /**
     * 对应功能的ID
     */
    @Column(name = "xfu_upload_fun_id")
    private Long xfuUploadFunId;

    /**
     * 使用位 0 待定 1 使用 2未使用
     */
    @Column(name = "xfu_upload_used")
    private Integer xfuUploadUsed;

    /**
     * 删除位
     */
    @Column(name = "xfu_del_status")
    private Boolean xfuDelStatus;
    
    @Column(name = "xfu_create")
    private Date xfuCreate;

    public static final String XFU_UPLOAD_ID = "xfuUploadId";

    public static final String XFU_UPLOAD_SRC = "xfuUploadSrc";

    public static final String XFU_UPLOAD_FUN_TYPE = "xfuUploadFunType";

    public static final String XFU_UPLOAD_FUN_ID = "xfuUploadFunId";

    public static final String XFU_UPLOAD_USED = "xfuUploadUsed";

    public static final String XFU_DEL_STATUS = "xfuDelStatus";
}