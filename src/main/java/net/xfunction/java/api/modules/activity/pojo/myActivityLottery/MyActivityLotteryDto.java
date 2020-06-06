package net.xfunction.java.api.modules.activity.pojo.myActivityLottery;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;

@Data
public class MyActivityLotteryDto {
	
	private String activityTitle;
	private String settings;
	
	public MyActivityLotteryDto(XfuActivity activity) {
		if(BaseUtils.isNotNull(activity)) {
			this.settings = activity.getXfuLotterySettings();
			this.activityTitle = activity.getXfuActivityTitle();
		}
	}
	
	public MyActivityLotteryDto() {}

}
