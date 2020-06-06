package net.xfunction.java.api.modules.activity.pojo.myActivityList;

import javax.persistence.Column;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class MyActivityListTicketDto {
	
	private Integer ticketSold;
	
	public MyActivityListTicketDto(XfuActivityTicket ticket) {
		if(BaseUtils.isNotNull(ticket)) {
		this.ticketSold = ticket.getXfuTicketSold();
		}
	}

	public MyActivityListTicketDto() {
		
	}
}
