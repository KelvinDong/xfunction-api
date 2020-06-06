package net.xfunction.java.api.modules.activity.service;

import java.io.IOException;
import java.util.List;

import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;
import net.xfunction.java.api.modules.activity.pojo.form.FormListDto;
import net.xfunction.java.api.modules.activity.pojo.form.FormPojo;
import net.xfunction.java.api.modules.activity.pojo.form.FormQuery;
import net.xfunction.java.api.modules.activity.pojo.myFavi.MySponsorListQuery;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorDto;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorQuery;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorVo;
import net.xfunction.java.api.modules.activity.pojo.ticket.TicketPojo;
import net.xfunction.java.api.modules.activity.pojo.ticket.TicketQuery;

public interface BaseService {

	Result createUpdateForm(XfuActivityForm form);

	List<FormListDto> selectForms(FormQuery query);

	List<TicketPojo> selectTickets(TicketQuery query);

	SponsorDto getSponsor(SponsorQuery query);

	void addUpdateSponsor(XfuActivitySponsor sponsor);

	String saveSponsorPic(SponsorVo vo) throws IllegalStateException, IOException;

	FormPojo selectForm(FormQuery query);

	Result createUpdateTicket(TicketPojo ticket);

	PageResultSet findMyFaviSponsors(MySponsorListQuery query);

	Boolean ReFaviSponsor(MySponsorListQuery query);

}
