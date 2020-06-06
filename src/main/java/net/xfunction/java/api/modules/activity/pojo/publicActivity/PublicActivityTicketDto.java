package net.xfunction.java.api.modules.activity.pojo.publicActivity;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class PublicActivityTicketDto {
	private Long ticketId;
	private Long activityId;
	private String ticketName;
	private Integer ticketSum;
	private String ticketRemark;
	private Integer ticketSold;
	/*
	 * 无status
	 */
	
	public PublicActivityTicketDto(XfuActivityTicket ticket) {
		if(BaseUtils.isNotNull(ticket)) {
			this.ticketId = ticket.getXfuTicketId();
			// this.activityId = ticket.getXfuTicketId();
			this.ticketName = ticket.getXfuTicketName();
			this.ticketSum = ticket.getXfuTicketSum();
			this.ticketRemark = ticket.getXfuTicketRemark();
			this.ticketSold = ticket.getXfuTicketSold();
		}
	}
	
	public PublicActivityTicketDto() {
		
	}
	
}
