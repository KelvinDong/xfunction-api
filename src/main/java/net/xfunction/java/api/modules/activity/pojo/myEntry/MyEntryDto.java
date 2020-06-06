package net.xfunction.java.api.modules.activity.pojo.myEntry;

import java.util.Date;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;
@Data
public class MyEntryDto {

	private Long entryId;
	private Long activityId;
	private Date entryCreate;
	private Date checkin;
	private String checkinCode;
	private Date cancel;
	
	private MyEntryActiviyDto activity;
	private MyEntryTicketDto ticket;
	
	public MyEntryDto(XfuActivityEntry entry) {
		if(BaseUtils.isNotNull(entry)) {
		this.entryId = entry.getXfuEntryId();
		this.activityId = entry.getXfuActivityId();
		this.entryCreate = entry.getXfuEntryCreate();
		this.checkin = entry.getXfuCheckin();
		this.checkinCode = entry.getXfuCheckinCode();
		this.cancel = entry.getXfuCancel();
		this.activity = new MyEntryActiviyDto(entry.getActivity());
		this.ticket = new MyEntryTicketDto(entry.getTicket());
		}
	}
	
	public MyEntryDto() {}
}
