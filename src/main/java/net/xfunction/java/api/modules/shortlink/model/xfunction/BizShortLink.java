	package net.xfunction.java.api.modules.shortlink.model.xfunction;

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
@Table(name = "biz_short_link")
public class BizShortLink {
    @Id
    @Column(name = "biz_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bizLinkId;

    @Column(name = "biz_link_url")
    private String bizLinkUrl;
    
    @Column(name = "biz_link_remark")
    private String bizLinkRemark;

    @Column(name = "biz_link_create_date")
    private Date bizLinkCreateDate;

    @Column(name = "biz_link_visit_date")
    private Date bizLinkVisitDate;

    @Column(name = "biz_link_visit_sum")
    private Long bizLinkVisitSum;
    
    @Column(name = "xfu_user_id")
    private Long xfuUserId;
    
    @Transient
    private String bizLinkId64;

    public static final String BIZ_LINK_ID = "bizLinkId";

    public static final String BIZ_LINK_URL = "bizLinkUrl";

    public static final String BIZ_LINK_CREATE_DATE = "bizLinkCreateDate";

    public static final String BIZ_LINK_VISIT_DATE = "bizLinkVisitDate";

    public static final String BIZ_LINK_VISIT_SUM = "bizLinkVisitSum";
}