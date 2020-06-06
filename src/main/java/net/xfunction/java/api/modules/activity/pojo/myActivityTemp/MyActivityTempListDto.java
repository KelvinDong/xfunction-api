package net.xfunction.java.api.modules.activity.pojo.myActivityTemp;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;

@Data
public class MyActivityTempListDto {
	
    private String activityTitle;
    private String activityPic;
    private Date activityStart;
    private Date activityEnd;
    private Date entryEnd;
    private String activityArea;
    private String activityAddress;
    private String activityTags;


    
    public MyActivityTempListDto(XfuActivityTemp temp) {
    	if(BaseUtils.isNotNull(temp)) {
	    	
	        this.activityTitle = temp.getXfuActivityTitle();
	        this.activityPic = temp.getXfuActivityPic();
	        this.activityStart = temp.getXfuActivityStart();
	        this.activityEnd = temp.getXfuActivityEnd();
	        this.entryEnd = temp.getXfuEntryEnd();
	        this.activityTags = temp.getXfuActivityTags();
	        this.activityArea = temp.getXfuActivityArea();
	        this.activityAddress = temp.getXfuActivityAddress();
	        
    	}
    }

	public MyActivityTempListDto() {
	}
    
}
