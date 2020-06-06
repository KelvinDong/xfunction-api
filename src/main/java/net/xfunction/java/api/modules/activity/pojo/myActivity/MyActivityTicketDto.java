package net.xfunction.java.api.modules.activity.pojo.myActivity;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class MyActivityTicketDto {

	private Long ticketId;
	private String ticketName;
	private Integer ticketSum;
	private Integer ticketSold;
	private Boolean ticketStatus;
	
	public MyActivityTicketDto(XfuActivityTicket ticket) {
		if(BaseUtils.isNotNull(ticket)) {
		this.ticketId = ticket.getXfuTicketId();
		this.ticketName = ticket.getXfuTicketName();
		this.ticketSum = ticket.getXfuTicketSum();
		this.ticketSold = ticket.getXfuTicketSold();
		this.ticketStatus = ticket.getXfuTicketStatus();
		}
	}
	
	public MyActivityTicketDto() {}
}
