package net.xfunction.java.api.modules.user.query;

import java.util.ArrayList;

import lombok.Data;

@Data
public class SlideQuery {
	private Integer move;
	private Integer action[];
	private Boolean clear = true; //默认清除
}
