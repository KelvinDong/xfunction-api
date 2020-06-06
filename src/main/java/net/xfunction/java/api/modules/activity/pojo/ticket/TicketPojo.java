package net.xfunction.java.api.modules.activity.pojo.ticket;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class TicketPojo {
	
	private Long userId;
	
	private Long ticketId;
	private Long activityId;
	private String ticketName;
	private String ticketRemark;
	private Boolean ticketStatus;
	private Integer ticketSum;
	private Integer ticketSold;
	
	public TicketPojo(XfuActivityTicket ticket) {
		if(BaseUtils.isNotNull(ticket)) {
			this.ticketId = ticket.getXfuTicketId();
			this.activityId = ticket.getXfuActivityId();
			this.ticketName = ticket.getXfuTicketName();
			this.ticketRemark = ticket.getXfuTicketRemark();
			this.ticketStatus = ticket.getXfuTicketStatus();
			this.ticketSum = ticket.getXfuTicketSum();
			this.ticketSold = ticket.getXfuTicketSold();
		}
	}
	
	public XfuActivityTicket convert() {
		XfuActivityTicket ticket = new XfuActivityTicket();
		ticket.setXfuTicketId(this.ticketId);
		ticket.setXfuActivityId(this.activityId);
		ticket.setXfuTicketName(this.ticketName);
		ticket.setXfuTicketRemark(this.ticketRemark);
		ticket.setXfuTicketStatus(this.ticketStatus);
		ticket.setXfuTicketSum(this.ticketSum);
		return ticket;
	}
	
	public TicketPojo(){}
}
