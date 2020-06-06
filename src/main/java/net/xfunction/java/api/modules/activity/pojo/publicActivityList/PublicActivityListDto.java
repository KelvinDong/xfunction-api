package net.xfunction.java.api.modules.activity.pojo.publicActivityList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityFormDto;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityTicketDto;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorDto;

@Data
public class PublicActivityListDto {

	private Long activityId;
    // private Long userId;
    private String activityTitle;
    private String activityPic;
    private Date activityStart;
    private Date activityEnd;
    private Date entryEnd;
    // private Long formId;
    private Date activityCreate;
    private String activityArea;
    private String activityAddress;
    private String activityTags;
    //private String activityContent;
    private Integer activityOrderDict;
    private Boolean isAd;
    // private Integer entryVisit;
    /*
     * 无以下
     * applyDict 
     * token 
     * create 
     * update
     */
    private SponsorDto  sponsor;
    //private  List<PublicActivityTicketDto> tickets;
    // private  PublicActivityFormDto form;
    
    public PublicActivityListDto(XfuActivity activity) {
    	if(BaseUtils.isNotNull(activity)) {
	    	this.activityId = activity.getXfuActivityId();
	        // this.userId = activity.getXfuUserId();
	        this.activityTitle = activity.getXfuActivityTitle();
	        this.activityPic = activity.getXfuActivityPic();
	        this.activityStart = activity.getXfuActivityStart();
	        this.activityEnd = activity.getXfuActivityEnd();
	        this.activityCreate = activity.getXfuActivityCreate();
	        this.entryEnd = activity.getXfuEntryEnd();
	        // this.formId = activity.getXfuFormId();
	        this.activityArea = activity.getXfuActivityArea();
	        this.activityAddress = activity.getXfuActivityAddress();
	        this.activityTags = activity.getXfuActivityTags();
	        //this.activityContent = activity.getXfuActivityContent();
	        this.activityOrderDict = activity.getXfuActivityOrderDict();
	        //this.entryVisit =  activity.getXfuEntryVisit();
	        this.sponsor = new SponsorDto(activity.getSponsor());
	        this.isAd = activity.getXfuIsAd();	        
    	}
    }

    public PublicActivityListDto() {}
}
