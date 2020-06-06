package net.xfunction.java.api.modules.activity.pojo.myFavi;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;

@Data
public class MySponsorListQuery extends BaseQuery<XfuActivitySponsor>{

	private Long userId;  // 我
	
	private Long sponsorId;   //我拟关注的对象
	
}
