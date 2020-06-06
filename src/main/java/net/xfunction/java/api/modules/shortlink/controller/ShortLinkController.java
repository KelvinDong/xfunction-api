package net.xfunction.java.api.modules.shortlink.controller;

import java.time.Duration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.Convert64Util;
import net.xfunction.java.api.core.utils.HttpUtil;
import net.xfunction.java.api.core.utils.RedisService;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.shortlink.model.xfunction.BizShortLink;
import net.xfunction.java.api.modules.shortlink.service.ShortLinkService;

@RestController
@Slf4j
@RequestMapping("/shortLink")
public class ShortLinkController {
	
	/**	
	@RequestParam用来处理 Content-Type 为 application/x-www-form-urlencoded 编码的内容，Content-Type默认为该属性。	
	注解@RequestParam接收的参数是来自requestHeader中，通常用于Get请求   健值对的形式  key=value
	
	注解@RequestBody接收的参数是来自requestBody中，即请求体		json
	 */
	
	
	@Resource 
	private ShortLinkService shortLinkService;
	@Resource
	private RedisService redisService;
	
	@PostMapping("/redirect")
	Result<String> redirect(@RequestBody BizShortLink bizShortLink,HttpServletRequest httpServletRequest){
		
		log.debug(bizShortLink.toString());
		
		//bizShortLink.getBizLinkId64()  合法性检验，包括 # ？
		//未来在此基础上增加访问统计功能，先在nginx中增加访问日志。 xfu.pw-> xfunction -> api 访问信息要保证完整，或者分成两个体系处理。
		String fromIpString = HttpUtil.getIpAddr(httpServletRequest);
		log.info("httpServletRequest from IP:"+(fromIpString==null?"":fromIpString));
		
		Double tempId = Convert64Util.decode(bizShortLink.getBizLinkId64());		
		
		bizShortLink = shortLinkService.getBizShortLinkByLinkId(tempId.longValue());		
		String reString = bizShortLink.getBizLinkUrl();		
		return Result.success(reString);
		
	}
	
	
	@PostMapping("/set")
	Result<BizShortLink> set(@RequestBody BizShortLink bizShortLink , HttpServletRequest httpServletRequest){
		
		log.debug(bizShortLink.toString());
		
		Integer defaultCacheValueInteger=1;
		String fromIpString = HttpUtil.getIpAddr(httpServletRequest);
		//Forbid  more request
		if(!BaseUtils.isEmpty(fromIpString)) {//get ip
			Object cacheValueObject = redisService.get(fromIpString);
			if(BaseUtils.isNull(cacheValueObject)) {
				redisService.set(fromIpString, defaultCacheValueInteger, Duration.ofSeconds(5).getSeconds());
				//go ahead
			}else {
				return Result.failure(ResultCodeEnum.MORE_REQUEST);
			}
		}else {
			//go ahead
		}
		
		BizShortLink returnBizShortLink = shortLinkService.getBizShortLink(bizShortLink.getBizLinkUrl());
		if(BaseUtils.isNotNull(returnBizShortLink)) {
			returnBizShortLink.setBizLinkId64(Convert64Util.encode(returnBizShortLink.getBizLinkId()));			
			return Result.success(returnBizShortLink);
		}else {
			return Result.failure(ResultCodeEnum.URL_NOT_REACHED);
		}		
		
	}
	

}
