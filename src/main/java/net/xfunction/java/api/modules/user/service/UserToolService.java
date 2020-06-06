package net.xfunction.java.api.modules.user.service;

import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.user.pojo.ToolPojo;

public interface UserToolService {

	void setToolSettings(ToolPojo pojo);

	Result getToolSettings(ToolPojo pojo);

}
