package net.xfunction.java.api.modules.questionnaire.pojo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PicFileQuery {
	private Long userId;
	private Long questionnaireId;
	private MultipartFile file;
}
