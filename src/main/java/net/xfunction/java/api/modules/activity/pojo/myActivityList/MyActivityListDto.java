package net.xfunction.java.api.modules.activity.pojo.myActivityList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;
import net.xfunction.java.api.modules.activity.pojo.myActivityTemp.MyActivityTempListDto;

@Data
public class MyActivityListDto {

	private Long activityId;
	private String activityTitle;
	private String activityPic;
	private Date activityStart;
	private Date activityEnd;
	private MyActivityTempListDto activityTemp;
	private Date entryEnd;
	private String activityArea;
	private String activityAddress;
	private Date activityCreate;
	private Date activityUpdate;
	private String activityTags;
	private Integer activityOrderDict;
	private String activityApplyDict;
	private Integer entryVisit;
	private String activityToken;
	private List<MyActivityListTicketDto> tickets;
	
	public MyActivityListDto(XfuActivity activity) {
		if(BaseUtils.isNotNull(activity)) {
		this.activityId = activity.getXfuActivityId();
		this.activityTitle = activity.getXfuActivityTitle();
		this.activityPic = activity.getXfuActivityPic();
		this.activityStart = activity.getXfuActivityStart();
		this.activityEnd = activity.getXfuActivityEnd();
		this.activityTemp = new MyActivityTempListDto(activity.getXfuActivityTemp());
		this.entryEnd = activity.getXfuEntryEnd();
		this.activityArea = activity.getXfuActivityArea();
		this.activityAddress = activity.getXfuActivityAddress();
		this.activityCreate = activity.getXfuActivityCreate();
		this.activityUpdate = activity.getXfuActivityUpdate();
		this.activityTags = activity.getXfuActivityTags();
		this.activityOrderDict = activity.getXfuActivityOrderDict();
		this.activityApplyDict = activity.getXfuActivityApplyDict();
		this.entryVisit = activity.getXfuEntryVisit();
		this.activityToken = activity.getXfuActivityToken();
		
		this.tickets = new ArrayList<MyActivityListTicketDto>();
		for (XfuActivityTicket ticket : activity.getTickets()) {
			this.tickets.add(new MyActivityListTicketDto(ticket));
		}
		}
		
	}
	
	public MyActivityListDto() {
		
	}
}
