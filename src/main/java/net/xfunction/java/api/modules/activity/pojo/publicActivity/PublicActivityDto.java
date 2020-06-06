package net.xfunction.java.api.modules.activity.pojo.publicActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorDto;

@Data
public class PublicActivityDto {
	private Long activityId;
    // private Long userId;
    private String activityTitle;
    private String activityPic;
    private Date activityStart;
    private Date activityEnd;
    private Date entryEnd;
    private Date activityCreate;
    // private Long formId;
    private String activityArea;
    private String activityAddress;
    private String activityTags;
    private String activityContent;
    private Integer activityOrderDict;
    private Integer entryVisit;
    /*
     * 无以下
     * applyDict 
     * token 
     * create 
     * update
     */
    private SponsorDto  sponsor;
    private  List<PublicActivityTicketDto> tickets;
    private  PublicActivityFormDto form;
    
    public PublicActivityDto(XfuActivity activity) {
    	if(BaseUtils.isNotNull(activity)) {
	    	this.activityId = activity.getXfuActivityId();
	        // this.userId = activity.getXfuUserId();
	        this.activityTitle = activity.getXfuActivityTitle();
	        this.activityPic = activity.getXfuActivityPic();
	        this.activityStart = activity.getXfuActivityStart();
	        this.activityEnd = activity.getXfuActivityEnd();
	        this.entryEnd = activity.getXfuEntryEnd();
	        this.activityCreate = activity.getXfuActivityCreate();
	        // this.formId = activity.getXfuFormId();
	        this.activityArea = activity.getXfuActivityArea();
	        this.activityAddress = activity.getXfuActivityAddress();
	        this.activityTags = activity.getXfuActivityTags();
	        this.activityContent = activity.getXfuActivityContent();
	        this.activityOrderDict = activity.getXfuActivityOrderDict();
	        this.entryVisit =  activity.getXfuEntryVisit();
	        
	        this.sponsor = new SponsorDto(activity.getSponsor());
	        this.form = new PublicActivityFormDto(activity.getForm());
	        this.tickets = new ArrayList<>();
	        for (XfuActivityTicket ticket : activity.getTickets()) {
				this.tickets.add(new PublicActivityTicketDto(ticket));
			}
	        
    	}
    }

	public PublicActivityDto() {
	}
    
}
