package net.xfunction.java.api.modules.activity.pojo.activityEntry;

import java.util.Date;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;

@Data
public class ActivityEntryDto {

	private Long entryId;
	private String entryContent;
	private Date entryCreate;
	private Date checkin;
	private Date cancel;
	private Long ticketId;

	
	public ActivityEntryDto(XfuActivityEntry entry) {
		if(BaseUtils.isNotNull(entry)) {
			this.entryId = entry.getXfuEntryId();
			this.ticketId = entry.getXfuTicketId();
			this.entryContent = entry.getXfuEntryContent();
			this.entryCreate = entry.getXfuEntryCreate();
			this.checkin = entry.getXfuCheckin();
			this.cancel = entry.getXfuCancel();
		}
	}
	
	public ActivityEntryDto() {}
	
}
