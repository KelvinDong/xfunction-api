package net.xfunction.java.api.modules.activity.model.xfunction;

import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = "xfu_activity_entry")
public class XfuActivityEntry {
    @Id
    @Column(name = "xfu_entry_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuEntryId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;

    @Column(name = "xfu_activity_id")
    private Long xfuActivityId;

    @Column(name = "xfu_ticket_id")
    private Long xfuTicketId;

    @Column(name = "xfu_entry_create")
    private Date xfuEntryCreate;

    @Column(name = "xfu_checkin")
    private Date xfuCheckin;

    @Column(name = "xfu_checkin_code")
    private String xfuCheckinCode;

    @Column(name = "xfu_entry_content")
    private String xfuEntryContent;
    
    @Column(name = "xfu_cancel")
    private Date xfuCancel;
    
    
    @Transient
    private XfuActivity activity;
    
    @Transient
    private XfuActivityTicket ticket;
    
    @Transient
    private XfuUser user;
    

    public static final String XFU_ENTRY_ID = "xfuEntryId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_ACTIVITY_ID = "xfuActivityId";

    public static final String XFU_TICKET_ID = "xfuTicketId";

    public static final String XFU_ENTRY_CREATE = "xfuEntryCreate";

    public static final String XFU_CHECKIN = "xfuCheckin";

    public static final String XFU_CHECKIN_CODE = "xfuCheckinCode";

    public static final String XFU_ENTRY_CONTENT = "xfuEntryContent";
}