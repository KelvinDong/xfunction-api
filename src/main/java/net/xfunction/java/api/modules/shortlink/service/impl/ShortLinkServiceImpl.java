package net.xfunction.java.api.modules.shortlink.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.Convert64Util;
import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;
import net.xfunction.java.api.modules.activity.pojo.myComment.CommentDto;
import net.xfunction.java.api.modules.activity.pojo.myComment.MyCommentsQuery;
import net.xfunction.java.api.modules.shortlink.mapper.xfunction.BizShortLinkMapper;
import net.xfunction.java.api.modules.shortlink.model.xfunction.BizShortLink;
import net.xfunction.java.api.modules.shortlink.pojo.ListQuery;
import net.xfunction.java.api.modules.shortlink.service.ShortLinkService;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

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
	public void updateShortLinkRemark(BizShortLink in,Long userId) {
		BizShortLink record = bizShortLinkMapper.selectByPrimaryKey(in.getBizLinkId());
		if(BaseUtils.isNotNull(record) && record.getXfuUserId().equals(userId)) {	
			record.setBizLinkRemark(in.getBizLinkRemark());
			bizShortLinkMapper.updateByPrimaryKey(record);
		}		
	}
	
	@Override
	public Result replaceShortLink(BizShortLink in,Long userId) {		
		BizShortLink record = bizShortLinkMapper.selectByPrimaryKey(in.getBizLinkId());
		if(BaseUtils.isNotNull(record) && record.getXfuUserId().equals(userId)) {			
			ResponseEntity<String> respString=null;
			try {
				respString = restTemplate.getForEntity(in.getBizLinkUrl(),String.class);	
			} catch (Exception e) {
				// TODO: 确认restTemplate的错误和返回意义，优化此处判断。 进一步考虑 ，页面内容是否符合法规要求；定时/抽检访问量大的页面，确保合法性，及时封杀。
			}						
			if(respString!=null && respString.getStatusCode()==HttpStatus.OK) {
				record.setBizLinkUrl(in.getBizLinkUrl());
				try {
					bizShortLinkMapper.updateByPrimaryKey(record);
				}catch (Exception e) {
					// 可能重复，直接报错处理了
					return Result.failure(ResultCodeEnum.URL_REPEATED);
				}	
			}else {
				return Result.failure(ResultCodeEnum.URL_NOT_REACHED);
			}
		}
		else {
			// 不作特别提醒
		}
		return Result.success();
	}
	
	
	@Override
	public BizShortLink getBizShortLinkByLinkUrl(String bizLinkUrl,Long xfuUserId){
		
		Weekend<BizShortLink> example = Weekend.of(BizShortLink.class);
		WeekendCriteria<BizShortLink, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(BizShortLink::getBizLinkUrl, bizLinkUrl);
		if(BaseUtils.isNull(xfuUserId))
			criteria.andIsNull(BizShortLink::getXfuUserId);
		else
			criteria.andEqualTo(BizShortLink::getXfuUserId,xfuUserId);
				
		return bizShortLinkMapper.selectOneByExample(example);
	}
	
	
	/***
	 * 创建
	 */
	@Override
	public BizShortLink getBizShortLink(String bizLinkUrl,Long xfuUserId) {
		
		BizShortLink existBizShortLink = this.getBizShortLinkByLinkUrl(bizLinkUrl,xfuUserId);
		
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
				bizShortLink.setXfuUserId(xfuUserId);
				bizShortLinkMapper.insertSelective(bizShortLink);
				return bizShortLink;
			}else {
				return null;
			}
			
			
		}
		
	}
	
	@Override
	public PageResultSet getMyUrls(ListQuery query) {
		
		PageHelper.orderBy("biz_link_id desc");
		
		Weekend<BizShortLink> example = Weekend.of(BizShortLink.class);
		WeekendCriteria<BizShortLink, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(BizShortLink::getXfuUserId, query.getUserId());
		
		Page<BizShortLink> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),true)
				.doSelectPage(() -> bizShortLinkMapper.selectByExample(example));
		
		page.getResult().forEach(item->{
			item.setBizLinkId64(Convert64Util.encode(item.getBizLinkId()));
		});
		
		PageResultSet resultSet = new PageResultSet();
		resultSet.setRows(page.getResult());
		resultSet.setTotal(page.getTotal());
		
		return resultSet;
		
		
	}

	
	
	
	
}
