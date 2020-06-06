package net.xfunction.java.api.modules.activity.pojo.myEntry;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;

@Data
public class MyEntryVo {

	private Long activityId;
	private Long ticketId;
	private Long userId;
	private String entryContent;
	
	public XfuActivityEntry convert() {
		XfuActivityEntry entry = new XfuActivityEntry();
		entry.setXfuActivityId(this.activityId);
		entry.setXfuTicketId(this.ticketId);
		entry.setXfuUserId(this.userId);
		entry.setXfuEntryContent(this.entryContent);
		return entry;
	}
	
	//4、导入
	private MultipartFile file;
}
