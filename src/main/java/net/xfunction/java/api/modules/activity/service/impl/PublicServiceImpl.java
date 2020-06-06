package net.xfunction.java.api.modules.activity.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityCommentMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivitySponsorMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.myComment.CommentDto;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityDto;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivityList.PublicActivityListDto;
import net.xfunction.java.api.modules.activity.pojo.publicActivityList.PublicActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.publicComment.PublicCommentQuery;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorDto;
import net.xfunction.java.api.modules.activity.service.PublicService;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class PublicServiceImpl implements PublicService {

	@Resource
	private  XfuActivityCommentMapper xfuActivityCommentMapper;

	@Resource
	private XfuActivityMapper xfuActivityMapper;
	
	@Resource 
	private XfuActivitySponsorMapper xfuActivitySponsorMapper;
	
	@Override
	public PublicActivityDto getPublicActity(PublicActivityQuery query) {
		xfuActivityMapper.updateActivityVisit(query);
		return new PublicActivityDto(xfuActivityMapper.selectPublicActivity(query));
	}
	
	@Override
	public PageResultSet getPublicActityList(PublicActivityListQuery query) {
		
		Page<XfuActivity> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityMapper.selectPublicActivityList(query));
		
		List<PublicActivityListDto> listDto = new ArrayList<>();
		for (XfuActivity activity : page.getResult()) {
			listDto.add(new PublicActivityListDto(activity));
		}
		
		PageResultSet<PublicActivityListDto> resultSet = new PageResultSet<>();
		resultSet.setRows(listDto);
		
		return resultSet;
	}
	
	// 上架的 某个活动方的活动列表
	@Override
	public PageResultSet getSponsorActityList(PublicActivityListQuery query) {
		
		Page<XfuActivity> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityMapper.selectSponsorActivityList(query));
		
		List<PublicActivityListDto> listDto = new ArrayList<>();
		for (XfuActivity activity : page.getResult()) {
			listDto.add(new PublicActivityListDto(activity));
		}
		
		PageResultSet<PublicActivityListDto> resultSet = new PageResultSet<>();
		resultSet.setRows(listDto);
		
		if(query.getOffset() ==0) {
			XfuActivitySponsor resultSponsor = xfuActivitySponsorMapper.selectByPrimaryKey(query.getSponsorId());		
			resultSet.getDataDictMap().put("sponsor", new SponsorDto(resultSponsor));
		}
		
		
		return resultSet;
	}
	
	@Override
	public PageResultSet getPublicComments(PublicCommentQuery query) {
		Page<XfuActivityComment> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityCommentMapper.selectPublicComments(query.getActivityId()));		
		List<CommentDto> listDto = new ArrayList<>();
		for (XfuActivityComment comment : page.getResult()) {
			listDto.add(new CommentDto(comment));
		}
		PageResultSet resultSet = new PageResultSet();
		resultSet.setRows(listDto);
		return resultSet;
		
		
	}
}
