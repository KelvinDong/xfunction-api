package net.xfunction.java.api.modules.user.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.RedisService;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.user.query.SlideQuery;

@Service
public class SlideService {
	
	@Resource
	private RedisService redisService;
	
	public Boolean vertify(SlideQuery slideQuery ,HttpServletRequest httpServletRequest) {
		
		//TODO 对slideQuery合法性
		
		HttpSession session = httpServletRequest.getSession();
		Integer xWidth = (Integer) redisService.get(session.getId());
		if(slideQuery.getClear()) redisService.del(session.getId());
		if(BaseUtils.isNotNull(xWidth) && Math.abs(xWidth-slideQuery.getMove())<=2) {
			//TODO  还要增加对slideQuery.action  针对Y轴的抖动标准差  与 平均值  相等时一般为0，表示可能非人为操作，要不相等。
			return true;
		}			
		return false;
		
	}

}
