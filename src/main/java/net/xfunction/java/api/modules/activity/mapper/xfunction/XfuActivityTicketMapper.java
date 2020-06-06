package net.xfunction.java.api.modules.activity.mapper.xfunction;

import java.util.List;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

public interface XfuActivityTicketMapper extends XfunMapper<XfuActivityTicket> {
	
	List<XfuActivityTicket> selectOKByActivityId(Long xfuActivityId);
	List<XfuActivityTicket> selectAllByActivityId(Long xfuActivityId);
	
	XfuActivityTicket selectTicketForAsso(Long xfuTicketId);
}