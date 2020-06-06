package net.xfunction.java.api.modules.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.user.mapper.xfunction.XfuUserMapper;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;
import net.xfunction.java.api.modules.user.pojo.ToolPojo;
import net.xfunction.java.api.modules.user.service.UserToolService;

@Service
public class UserToolServiceImpl implements UserToolService {
	
	@Resource
	private XfuUserMapper xfuUserMapper;
	
	@Override
	public void setToolSettings(ToolPojo pojo) {
		
		XfuUser user = new XfuUser();
		user.setXfuUserId(pojo.getUserId());
		user.setXfuToolSettings(pojo.getToolSettings());
		xfuUserMapper.updateByPrimaryKeySelective(user);
		
	}
	
	@Override
	public Result getToolSettings(ToolPojo pojo) {
		XfuUser user = xfuUserMapper.selectByPrimaryKey(pojo.getUserId());
		pojo.setToolSettings(user.getXfuToolSettings());
		return Result.success(pojo);
	}
	

}
