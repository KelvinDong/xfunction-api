package net.xfunction.java.api.modules.activity.pojo.myActivityLottery;

import java.util.Date;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityLottery;

@Data
public class LotteryResultDto {

	private String result;
	private String remark;
	private Date create;
	
	public LotteryResultDto(XfuActivityLottery lottery){
		if(BaseUtils.isNotNull(lottery)) {
			this.result = lottery.getXfuLotteryResult();
			this.create = lottery.getXfuLotteryCreate();
			this.remark = lottery.getXfuLotteryRemark();
		}
	}
	public LotteryResultDto(){}
	
}
