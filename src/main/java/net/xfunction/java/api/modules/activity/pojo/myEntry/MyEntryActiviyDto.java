package net.xfunction.java.api.modules.activity.pojo.myEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;

@Data
public class MyEntryActiviyDto {
	// private Long activityId;
	private String activityTitle;
	private String activityPic;
	private Date activityStart;
	private Date activityEnd;
	// private Date activityEndUsed;
	private String activityArea;
	private String activityAddress;
	// private Date activityCreate;
	// private Date activityUpdate;
	private String activityTags;
	private Integer activityOrderDict;
	// private String activityApplyDict;
	// private Integer entryVisit;
	// private String activityToken;
	// private List<MyActivityListTicketDto> tickets;

	public MyEntryActiviyDto(XfuActivity activity) {
		if (BaseUtils.isNotNull(activity)) {

			this.activityTitle = activity.getXfuActivityTitle();
			this.activityPic = activity.getXfuActivityPic();
			this.activityStart = activity.getXfuActivityStart();
			this.activityEnd = activity.getXfuActivityEnd();

			this.activityArea = activity.getXfuActivityArea();
			this.activityAddress = activity.getXfuActivityAddress();

			this.activityTags = activity.getXfuActivityTags();
			this.activityOrderDict = activity.getXfuActivityOrderDict();

		}

	}

	public MyEntryActiviyDto() {
	}
}
