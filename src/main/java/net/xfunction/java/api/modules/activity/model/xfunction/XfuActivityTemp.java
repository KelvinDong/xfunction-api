package net.xfunction.java.api.modules.activity.model.xfunction;

import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = "xfu_activity_temp")
public class XfuActivityTemp {
    @Id
    @Column(name = "xfu_activity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuActivityId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;

    @Column(name = "xfu_activity_title")
    private String xfuActivityTitle;

    @Column(name = "xfu_activity_pic")
    private String xfuActivityPic;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "xfu_activity_start")
    private Date xfuActivityStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "xfu_activity_end")
    private Date xfuActivityEnd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "xfu_entry_start")
    private Date xfuEntryStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "xfu_entry_end")
    private Date xfuEntryEnd;
    
    @Column(name = "xfu_Form_id")
    private Long xfuFormId;

    @Column(name = "xfu_activity_area")
    private String xfuActivityArea;

    @Column(name = "xfu_activity_address")
    private String xfuActivityAddress;

    @Column(name = "xfu_activity_create")
    private Date xfuActivityCreate;

    @Column(name = "xfu_activity_update")
    private Date xfuActivityUpdate;

    @Column(name = "xfu_activity_tags")
    private String xfuActivityTags;

    @Column(name = "xfu_activity_content")
    private String xfuActivityContent;
    

    public static final String XFU_ACTIVITY_ID = "xfuActivityId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_ACTIVITY_TITLE = "xfuActivityTitle";

    public static final String XFU_ACTIVITY_PIC = "xfuActivityPic";

    public static final String XFU_ACTIVITY_START = "xfuActivityStart";

    public static final String XFU_ACTIVITY_END = "xfuActivityEnd";

    public static final String XFU_ENTRY_START = "xfuEntryStart";

    public static final String XFU_ENTRY_END = "xfuEntryEnd";

    public static final String XFU_ACTIVITY_AREA = "xfuActivityArea";

    public static final String XFU_ACTIVITY_ADDRESS = "xfuActivityAddress";

    public static final String XFU_ACTIVITY_CREATE = "xfuActivityCreate";

    public static final String XFU_ACTIVITY_UPDATE = "xfuActivityUpdate";

    public static final String XFU_ACTIVITY_TAGS = "xfuActivityTags";

    public static final String XFU_ACTIVITY_CONTENT = "xfuActivityContent";
}