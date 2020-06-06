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
@Table(name = "xfu_activity_lottery")
public class XfuActivityLottery {
    @Id
    @Column(name = "xfu_lottery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuLotteryId;

    @Column(name = "xfu_activity_id")
    private Long xfuActivityId;

    @Column(name = "xfu_lottery_create")
    private Date xfuLotteryCreate;

    @Column(name = "xfu_lottery_remark")
    private String xfuLotteryRemark;

    @Column(name = "xfu_lottery_result")
    private String xfuLotteryResult;

    public static final String XFU_LOTTERY_ID = "xfuLotteryId";

    public static final String XFU_ACTIVITY_ID = "xfuActivityId";

    public static final String XFU_LOTTERY_CREATE = "xfuLotteryCreate";

    public static final String XFU_LOTTERY_REMARK = "xfuLotteryRemark";

    public static final String XFU_LOTTERY_RESULT = "xfuLotteryResult";
}