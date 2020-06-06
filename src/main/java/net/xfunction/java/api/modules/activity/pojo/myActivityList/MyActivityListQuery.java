package net.xfunction.java.api.modules.activity.pojo.myActivityList;


import lombok.Getter;
import lombok.Setter;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;

@Getter
@Setter
public class MyActivityListQuery extends BaseQuery<XfuActivity>{   
	
	//1、我的活动搜索组合条件 
	//
	private Long userId;
	private String activityOrderDict;
	private String activityApplyDict;
	private Boolean showExpired;// true 显示未过期的
    
	
	//2、上架还是下架
	// *userId,与activityId组合查找
	private Long activityId;
	private Integer myApplyId;  
}
