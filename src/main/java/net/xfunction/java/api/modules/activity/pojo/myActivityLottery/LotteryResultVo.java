package net.xfunction.java.api.modules.activity.pojo.myActivityLottery;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityLottery;

@Data
public class LotteryResultVo {
	private Long userId;
	private Long activityId;
	private String result;
	private String remark;
	
	public XfuActivityLottery convert() {
		XfuActivityLottery result = new XfuActivityLottery();
		result.setXfuActivityId(this.activityId);
		result.setXfuLotteryResult(this.result);
		result.setXfuLotteryRemark(this.remark);
		return result;
	}
}
