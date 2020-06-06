package net.xfunction.java.api.modules.activity.pojo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PicFileQuery {
	private Integer funType =1; // 默认是活动的图片 3 活动抽奖图片
	private Long activityId;
	private MultipartFile file;
}
