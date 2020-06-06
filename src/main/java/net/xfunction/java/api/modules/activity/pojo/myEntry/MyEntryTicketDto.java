package net.xfunction.java.api.modules.activity.pojo.myEntry;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class MyEntryTicketDto {

	private String ticketName;
	
	public MyEntryTicketDto(XfuActivityTicket ticket) {
		if(BaseUtils.isNotNull(ticket)) {
		this.ticketName = ticket.getXfuTicketName();
		}
	}
	public MyEntryTicketDto() {}
}
