package net.xfunction.java.api.modules.activity.model.xfunction;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = "xfu_activity_ticket")
public class XfuActivityTicket {
    @Id
    @Column(name = "xfu_ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuTicketId;

    @Column(name = "xfu_activity_id")
    private Long xfuActivityId;
    

    @Column(name = "xfu_ticket_name")
    private String xfuTicketName;

    @Column(name = "xfu_ticket_sum")
    private Integer xfuTicketSum;

    @Column(name = "xfu_ticket_remark")
    private String xfuTicketRemark;

    @Column(name = "xfu_ticket_sold")
    private Integer xfuTicketSold;

    /**
     * 0 未启用
     */
    @Column(name = "xfu_ticket_status")
    private Boolean xfuTicketStatus;

    public static final String XFU_TICKET_ID = "xfuTicketId";

    public static final String XFU_ACTIVITY_ID = "xfuActivityId";

    public static final String XFU_TICKET_NAME = "xfuTicketName";

    public static final String XFU_TICKET_SUM = "xfuTicketSum";

    public static final String XFU_TICKET_REMARK = "xfuTicketRemark";

    public static final String XFU_TICKET_SOLD = "xfuTicketSold";

    public static final String XFU_TICKET_STATUS = "xfuTicketStatus";
}