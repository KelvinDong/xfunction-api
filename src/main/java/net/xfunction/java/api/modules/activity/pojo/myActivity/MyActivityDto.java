package net.xfunction.java.api.modules.activity.pojo.myActivity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class MyActivityDto {

	private List<MyActivityTicketDto> tickets;
	
	private MyActivityFormDto form;
	
	public MyActivityDto(XfuActivity activity) {
		
		if(BaseUtils.isNotNull(activity)) {
		this.form = new MyActivityFormDto(activity.getForm());
		
		this.tickets = new ArrayList<>();
		for (XfuActivityTicket ticket: activity.getTickets()) {
			this.tickets.add(new MyActivityTicketDto(ticket));
		}
		}
		
	}
	public MyActivityDto() {}
}
