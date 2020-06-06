package net.xfunction.java.api.modules.activity.pojo.myActivityTemp;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;

@Data
public class MyActivityTempPojo {
	private Long activityId;
    private Long userId;
    private String activityTitle;
    private String activityPic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date activityStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date activityEnd;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date entryEnd;
    private Long formId;
    private String activityArea;
    private String activityAddress;
    private String activityTags;
    private String activityContent;
    
    public XfuActivityTemp convertToXfuActivityTemp() {
    	XfuActivityTemp temp = new XfuActivityTemp();
    	temp.setXfuActivityId(this.activityId);
    	temp.setXfuUserId(this.userId);
    	temp.setXfuActivityTitle(this.activityTitle);
    	temp.setXfuActivityPic(this.activityPic);
    	temp.setXfuActivityStart(this.activityStart);
    	temp.setXfuActivityEnd(this.activityEnd);
    	temp.setXfuEntryEnd(this.entryEnd);
    	temp.setXfuFormId(this.formId);
    	temp.setXfuActivityArea(this.activityArea);
    	temp.setXfuActivityAddress(this.activityAddress);
    	temp.setXfuActivityTags(this.activityTags);
    	temp.setXfuActivityContent(this.activityContent);
    	return temp;
    }
    
    public MyActivityTempPojo(XfuActivityTemp temp) {
    	if(BaseUtils.isNotNull(temp)) {
	    	this.activityId = temp.getXfuActivityId();
	        this.userId = temp.getXfuUserId();
	        this.activityTitle = temp.getXfuActivityTitle();
	        this.activityPic = temp.getXfuActivityPic();
	        this.activityStart = temp.getXfuActivityStart();
	        this.activityEnd = temp.getXfuActivityEnd();
	        this.entryEnd = temp.getXfuEntryEnd();
	        this.formId = temp.getXfuFormId();
	        this.activityArea = temp.getXfuActivityArea();
	        this.activityAddress = temp.getXfuActivityAddress();
	        this.activityTags = temp.getXfuActivityTags();
	        this.activityContent = temp.getXfuActivityContent();
    	}
    }

	public MyActivityTempPojo() {
	}
    
}
