package net.xfunction.java.api.modules.activity.pojo.activityEntry;


import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;

@Data
public class ActivityEntryQuery extends BaseQuery<XfuActivityEntry>{   
	
	//1、搜索条件
	private Long userId;
	private Long activityId;
	private Long ticketId;
	private Boolean checkin;
	private Boolean cancel;
	private String queryStr;
	private Long[] tickets;  // for lottery
	
	//2、签到  主办扫描参会者的个人签到码，id 是用户的报名ID， code 是checkincode
	/// + userid 是不是我的活动
	private Long id;
	private String code;
	
	//3、代签到
	// + userid 
	private Long entryId;
	
	
    
}
