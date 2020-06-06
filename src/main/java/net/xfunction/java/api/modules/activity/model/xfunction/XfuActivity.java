package net.xfunction.java.api.modules.activity.model.xfunction;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = "xfu_activity")
public class XfuActivity {
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

    @Column(name = "xfu_activity_start")
    private Date xfuActivityStart;

    @Column(name = "xfu_activity_end")
    private Date xfuActivityEnd;
    
    

    @Column(name = "xfu_entry_start")
    private Date xfuEntryStart;

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

    /**
     * 0，1，500  仅能展示> 0的数据
主要是管理位
用户可以操作 1/500 -> 0 
     */
    @Column(name = "xfu_activity_order_dict")
    private Integer xfuActivityOrderDict;

    /**
     * 未提交（编辑） 0，   提交核实中 1， 审核失败2，审核成功 3，
审核过程放在另外一张表中；
0->1  用户提交
1->2  管理员
1->3 管理员(从临时表拷过来，同时将推荐位改为1）

1->0 用户编辑  
2->0 用户编辑
3->0 用户编辑
     */
    @Column(name = "xfu_activity_apply_dict")
    private String xfuActivityApplyDict;


    @Column(name = "xfu_entry_visit")
    private Integer xfuEntryVisit;

    @Column(name = "xfu_activity_content")
    private String xfuActivityContent;
    
    @Column(name = "xfu_activity_token")
    private String xfuActivityToken;
    
    @Column(name = "xfu_lottery_settings")
    private String xfuLotterySettings;
    @Column(name = "xfu_wall_settings")
    private String xfuWallSettings;
    
    @Column(name = "xfu_is_ad")
    private Boolean xfuIsAd;
    
    
    //////以下/////非数据库属性
    @Transient
    private List<XfuActivityTicket> tickets;
    
    @Transient
    private XfuActivitySponsor sponsor;
    
    @Transient
    private XfuActivityForm form;
    
    //给到我的活动列表中使用
    @Transient
    private XfuActivityTemp xfuActivityTemp;
    
    public XfuActivity(XfuActivityTemp temp) {
		this.xfuActivityAddress = temp.getXfuActivityAddress();
		this.xfuActivityArea = temp.getXfuActivityArea();
		this.xfuActivityContent = temp.getXfuActivityContent();
		this.xfuActivityEnd = temp.getXfuActivityEnd();
		this.xfuActivityId = temp.getXfuActivityId();
		this.xfuActivityPic = temp.getXfuActivityPic();
		this.xfuActivityStart = temp.getXfuActivityStart();
		this.xfuActivityTags = temp.getXfuActivityTags();
		this.xfuActivityTitle = temp.getXfuActivityTitle();
		this.xfuEntryEnd = temp.getXfuEntryEnd();
		this.xfuEntryStart = temp.getXfuEntryStart();
		this.xfuUserId = temp.getXfuUserId();
		this.xfuFormId = temp.getXfuFormId();
	}
    
    public XfuActivity() {
    	
    }

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

    public static final String XFU_ACTIVITY_ORDER_DICT = "xfuActivityOrderDict";

    public static final String XFU_ACTIVITY_APPLY_DICT = "xfuActivityApplyDict";

    public static final String XFU_ENTRY_VISIT = "xfuEntryVisit";

    public static final String XFU_ACTIVITY_CONTENT = "xfuActivityContent";
}