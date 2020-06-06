package net.xfunction.java.api.modules.activity.pojo.myEntry;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;

@Data
public class MyEntryQuery extends BaseQuery<XfuActivityEntry>{   
	
	// 一、获得用户的报名
	private Long userId;
	
	// 二、取消报名时，batis中与二次确认
	// *userid
	private Long entryId;
	
	// 三、 参会者扫描主办方提供的活动现场码  id是活动id, code是activitytoken
	// * userid, 找到报名记录，判断是我的报名吗
	private Long id;
	private String code;
}
