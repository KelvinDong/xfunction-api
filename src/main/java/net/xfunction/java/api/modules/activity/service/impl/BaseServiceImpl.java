package net.xfunction.java.api.modules.activity.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.catalina.LifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.xfunction.java.api.core.enums.FileType;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityCommentMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityFaviMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityFormMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivitySponsorMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityTempMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityTicketMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuUploadImgMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityFavi;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuUploadImg;
import net.xfunction.java.api.modules.activity.pojo.PicFileQuery;
import net.xfunction.java.api.modules.activity.pojo.form.FormListDto;
import net.xfunction.java.api.modules.activity.pojo.form.FormPojo;
import net.xfunction.java.api.modules.activity.pojo.form.FormQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListDto;
import net.xfunction.java.api.modules.activity.pojo.myFavi.MySponsorListDto;
import net.xfunction.java.api.modules.activity.pojo.myFavi.MySponsorListQuery;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorDto;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorQuery;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorVo;
import net.xfunction.java.api.modules.activity.pojo.ticket.TicketPojo;
import net.xfunction.java.api.modules.activity.pojo.ticket.TicketQuery;
import net.xfunction.java.api.modules.activity.service.BaseService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;


@Service
public class BaseServiceImpl implements BaseService {

	@Value("${xfunction.activity.images}")
	private String localActivityPath;
	
	@Value("${xfunction.activity.form.limit}")
	private Integer formLimit;
	
	@Resource 
	XfuActivityFormMapper xfuActivityFormMapper;
	
	@Resource
	XfuActivityTicketMapper xfuActivityTicketMapper;
	
	@Resource 
	XfuActivitySponsorMapper xfuActivitySponsorMapper;
	
	@Resource 
	private XfuUploadImgMapper xfuUploadImgMapper;
	
	@Resource
	private XfuActivityFaviMapper xfuActivityFaviMapper;
	
	@Resource 
	private XfuActivityMapper xfuActivityMapper;
	
	
	
	@Override
	public Result createUpdateForm(XfuActivityForm form) {		
		if(BaseUtils.isNull(form.getXfuFormId())) {			
			if(this.formsAccount(form.getXfuUserId())>= formLimit) 
				return Result.failure(ResultCodeEnum.FAILD, "报名模板仅能创建10个");			
			xfuActivityFormMapper.insertSelective(form);			
		}else {
			xfuActivityFormMapper.updateByPrimaryKeySelective(form);
		}
		return Result.success(new FormPojo(form));
	}
	
	@Override
	public List<FormListDto> selectForms(FormQuery query) {
		Weekend<XfuActivityForm> example = Weekend.of(XfuActivityForm.class);
		WeekendCriteria<XfuActivityForm, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityForm::getXfuUserId, query.getUserId());
		// example.setOrderByClause("xfu_form_id desc");
		Page<XfuActivityForm> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityFormMapper.selectByExample(example)); 
		
		List<FormListDto> listDto = new ArrayList<>(); 
		for (XfuActivityForm xfuActivityForm : page.getResult()) {
			listDto.add(new FormListDto(xfuActivityForm));
		}
		
		return listDto;
	}
	
	@Override
	public FormPojo selectForm(FormQuery query) {
		Weekend<XfuActivityForm> example = Weekend.of(XfuActivityForm.class);
		WeekendCriteria<XfuActivityForm, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityForm::getXfuFormId, query.getFormId());
		criteria.andEqualTo(XfuActivityForm::getXfuUserId, query.getUserId());
		
		return new FormPojo(xfuActivityFormMapper.selectOneByExample(example));
	}
	
	// 为了限制个人表单数
	private Integer formsAccount(Long xfuUserId) {
		Weekend<XfuActivityForm> example = Weekend.of(XfuActivityForm.class);
		WeekendCriteria<XfuActivityForm, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityForm::getXfuUserId, xfuUserId);
		return xfuActivityFormMapper.selectCountByExample(example);
	}
	
	@Override
	public Result createUpdateTicket(TicketPojo p) {
		
		XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(p.getActivityId());
		if(!activity.getXfuUserId().equals(p.getUserId())) return Result.failure(ResultCodeEnum.FAILD);
		
		XfuActivityTicket ticket = p.convert();
		
		if(BaseUtils.isNull(ticket.getXfuTicketId())) {	
			xfuActivityTicketMapper.insertSelective(ticket);
		}else {
			xfuActivityTicketMapper.updateByPrimaryKeySelective(ticket);
		}
		return Result.success(ticket);
	}
	
	@Override
	public List<TicketPojo> selectTickets(TicketQuery query){
		
		List<TicketPojo> listDto = new ArrayList<>();
		
		XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(query.getActivityId());
		if(BaseUtils.isNull(activity) || !activity.getXfuUserId().equals(query.getUserId())) return listDto;
		
		Weekend<XfuActivityTicket> example = Weekend.of(XfuActivityTicket.class);
		WeekendCriteria<XfuActivityTicket, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityTicket::getXfuActivityId, query.getActivityId());
		for(XfuActivityTicket ticket: xfuActivityTicketMapper.selectByExample(example) ) {
			listDto.add(new TicketPojo(ticket));
		}
		return listDto;
		
	}
	
	
	@Override
	public SponsorDto getSponsor(SponsorQuery query) {
		XfuActivitySponsor resultSponsor = xfuActivitySponsorMapper.selectByPrimaryKey(query.getSponsorId());
		return new SponsorDto(resultSponsor);
	}
	
	@Override
	public void addUpdateSponsor(XfuActivitySponsor sponsor) {
		
		sponsor.setXfuSponsorUpdate(new Date()); //强制更新
		if(xfuActivitySponsorMapper.updateByPrimaryKeySelective(sponsor)<=0) {
			xfuActivitySponsorMapper.insertSelective(sponsor);
		}
		
		//标注已经失效的内容图片
		Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
		WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
		c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.SPONSORLOGO.getValue());//主办方的Logo
		c.andEqualTo(XfuUploadImg::getXfuUploadFunId,sponsor.getXfuUserId());
		List<Integer> para = new ArrayList<>();
		para.add(0);
		para.add(1);
		c.andIn(XfuUploadImg::getXfuUploadUsed, para);//使用中的，待确认	
		List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
		for (XfuUploadImg xfuUploadImg : imageList) {
			if(sponsor.getXfuSponsorLogo().contains(xfuUploadImg.getXfuUploadSrc())) { 
				xfuUploadImg.setXfuUploadUsed(1);
				xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
			}else {			
				xfuUploadImg.setXfuUploadUsed(2);
				xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
			}
		}
	}
	
	@Override
	public String saveSponsorPic(SponsorVo vo) throws IllegalStateException, IOException {
		// 文件存储
		MultipartFile file = vo.getFile();
		String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String fileName = UUID.randomUUID() + suffixName;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		String sub = df.format(new Date());
		fileName = "/" + sub + "/" + fileName;
		File dest = new File(localActivityPath + fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		file.transferTo(dest);
		
		XfuUploadImg img = new XfuUploadImg();
		img.setXfuUploadFunType(FileType.SPONSORLOGO.getValue());  //活动主办方的logo
		img.setXfuUploadFunId(vo.getSponsorId());
		img.setXfuUploadUsed(0); // 0待定，1使用，2未使用
		img.setXfuUploadSrc(fileName);
		xfuUploadImgMapper.insertSelective(img);
		return fileName;
	}
	
	@Override
	public PageResultSet findMyFaviSponsors(MySponsorListQuery query) {
		// para: userid
		Page<XfuActivitySponsor> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivitySponsorMapper.selectByFavi(query.getUserId()));
		
		List<MySponsorListDto> listDto = new ArrayList<>();
		for(XfuActivitySponsor s: page.getResult()) {
			listDto.add(new MySponsorListDto(s));
		}
		PageResultSet<MySponsorListDto> resultSet = new PageResultSet<>();
		resultSet.setRows(listDto);
		return resultSet;
	}
	
	// toggle 关注状态
	@Override
	public Boolean ReFaviSponsor(MySponsorListQuery query) {	
		Boolean isAdd = true;
		Weekend<XfuActivityFavi> example = Weekend.of(XfuActivityFavi.class);
		WeekendCriteria<XfuActivityFavi, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityFavi::getXfuUserId,query.getUserId());
		criteria.andEqualTo(XfuActivityFavi::getXfuSonsorId,query.getSponsorId());
		XfuActivityFavi favi = xfuActivityFaviMapper.selectOneByExample(example); 
		if(BaseUtils.isNotNull(favi)) {
			if(favi.getXfuFaviStatus()) isAdd = false;
			favi.setXfuFaviStatus(!favi.getXfuFaviStatus());
			favi.setXfuFaviUpdate(new Date());
			xfuActivityFaviMapper.updateByPrimaryKeySelective(favi);
		} else {
			favi = new XfuActivityFavi();
			favi.setXfuUserId(query.getUserId());
			favi.setXfuSonsorId(query.getSponsorId());
			xfuActivityFaviMapper.insertSelective(favi);
		}
		return isAdd;
	}
	
	
	
}
