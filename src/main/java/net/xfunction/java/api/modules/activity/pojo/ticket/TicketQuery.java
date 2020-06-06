package net.xfunction.java.api.modules.activity.pojo.ticket;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class TicketQuery extends BaseQuery<XfuActivityTicket>{   
	private Long activityId;
	private Long userId;
}
