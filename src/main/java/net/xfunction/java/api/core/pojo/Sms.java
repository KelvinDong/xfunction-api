package net.xfunction.java.api.core.pojo;

import lombok.Data;

@Data
public class Sms {

	private String mobile;
	private String code;
		
	private Integer move;
	private Integer action[];
}
