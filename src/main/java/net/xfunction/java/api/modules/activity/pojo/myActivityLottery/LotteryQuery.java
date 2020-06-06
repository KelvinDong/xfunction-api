package net.xfunction.java.api.modules.activity.pojo.myActivityLottery;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityLottery;

@Data
public class LotteryQuery extends BaseQuery<XfuActivityLottery> {

	private Long userId;
	private Long activityId;
	
}
