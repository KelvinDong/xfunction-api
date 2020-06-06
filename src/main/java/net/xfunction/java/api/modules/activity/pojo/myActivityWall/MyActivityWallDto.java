package net.xfunction.java.api.modules.activity.pojo.myActivityWall;

import java.util.List;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;

@Data
public class MyActivityWallDto {
	private String wallSettings;
	private Integer total;
	private List<String> names;
	private List<String> heads;
	
}
