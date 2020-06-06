package net.xfunction.java.api.modules.shortlink.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.druid.sql.visitor.functions.Now;

import net.xfunction.java.api.modules.shortlink.mapper.xfunction.BizShortLinkMapper;
import net.xfunction.java.api.modules.shortlink.model.xfunction.BizShortLink;
import net.xfunction.java.api.modules.shortlink.service.ShortLinkService;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {

	
	@Resource 	
	private BizShortLinkMapper bizShortLinkMapper;
	
	@Resource
    private RestTemplate restTemplate;

	
	/**
	 * 解决了高并发的问题，但记数存在问题了,所以暂屏蔽缓存功能
	 */
	@Override
	//@Cacheable(value="bizLinkId",key="'bizLinkId-'.concat(#bizLinkId)", condition ="#bizLinkId!=null",unless ="#result==null")
	public BizShortLink getBizShortLinkByLinkId(Long bizLinkId) {
		
		BizShortLink retrunBizShortLink = new BizShortLink();		
		retrunBizShortLink.setBizLinkId(bizLinkId);		
		retrunBizShortLink = bizShortLinkMapper.selectOne(retrunBizShortLink);
		
		
		retrunBizShortLink.setBizLinkVisitSum(retrunBizShortLink.getBizLinkVisitSum()+1);
		retrunBizShortLink.setBizLinkVisitDate(new Date());
		bizShortLinkMapper.updateByPrimaryKeySelective(retrunBizShortLink);
		
		return retrunBizShortLink;
		
	}
	
	@Override
	public BizShortLink getBizShortLinkByLinkUrl(String bizLinkUrl){
		
		BizShortLink retrunBizShortLink = new BizShortLink();
		
		retrunBizShortLink.setBizLinkUrl(bizLinkUrl);
		
		return bizShortLinkMapper.selectOne(retrunBizShortLink);
	}
	
	@Override
	public BizShortLink getBizShortLink(String bizLinkUrl) {
		
		BizShortLink existBizShortLink = this.getBizShortLinkByLinkUrl(bizLinkUrl);
		
		if(existBizShortLink!=null) return existBizShortLink;
		else {
			ResponseEntity<String> respString=null;
			try {
				respString = restTemplate.getForEntity(bizLinkUrl,String.class);	
			} catch (Exception e) {
				// TODO: 确认restTemplate的错误和返回意义，优化此处判断。 进一步考虑 ，页面内容是否符合法规要求；定时/抽检访问量大的页面，确保合法性，及时封杀。
			}
						
			if(respString!=null && respString.getStatusCode()==HttpStatus.OK) {
				BizShortLink bizShortLink = new BizShortLink();
				bizShortLink.setBizLinkUrl(bizLinkUrl);
				bizShortLink.setBizLinkVisitSum(0L);
				bizShortLinkMapper.insertSelective(bizShortLink);
				return bizShortLink;
			}else {
				return null;
			}
			
			
		}
		
	}
	
	
	
}
