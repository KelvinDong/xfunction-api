package net.xfunction.java.api.modules.activity.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.enums.FileType;
import net.xfunction.java.api.core.service.DataDictionaryManagementService;
import net.xfunction.java.api.core.socket.MyHandler;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.DateUtil;
import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.activity.controller.ActivityController;
import net.xfunction.java.api.modules.activity.enums.XfuActivityApplyDict;
import net.xfunction.java.api.modules.activity.enums.XfuActivityOrderDict;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityCommentMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityEntryMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityLotteryMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityTempMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuActivityTicketMapper;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuUploadImgMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityForm;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityLottery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTicket;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuUploadImg;
import net.xfunction.java.api.modules.activity.pojo.PicFileQuery;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentQuery;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentVo;
import net.xfunction.java.api.modules.activity.pojo.activityEntry.ActivityEntryDto;
import net.xfunction.java.api.modules.activity.pojo.activityEntry.ActivityEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.form.FormListDto;
import net.xfunction.java.api.modules.activity.pojo.myActivity.MyActivityDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryResultDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryResultVo;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityEntryForLottery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryVo;
import net.xfunction.java.api.modules.activity.pojo.myActivityTemp.MyActivityTempPojo;
import net.xfunction.java.api.modules.activity.pojo.myActivityTemp.MyActivityTempQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityWall.MyActivityWallDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityWall.MyActivityWallQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityWall.MyActivityWallVo;
import net.xfunction.java.api.modules.activity.pojo.myComment.CommentDto;
import net.xfunction.java.api.modules.activity.pojo.myComment.MyCommentsQuery;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryDto;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryVo;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityDto;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityQuery;
import net.xfunction.java.api.modules.activity.pojo.publicActivity.PublicActivityTicketDto;
import net.xfunction.java.api.modules.activity.service.MainService;
import net.xfunction.java.api.modules.activity.service.PublicService;
import net.xfunction.java.api.modules.user.mapper.xfunction.XfuUserMapper;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Slf4j
public class MainServiceImpl implements MainService {

	@Value("${xfunction.activity.images}")
	private String localActivityPath;
	
	@Value("${xfunction.activity.temp}")
	private String localActivityTemp;
	
	@Resource
    private MyHandler myHandler;


	@Resource
	private XfuActivityTempMapper xfuActivityTempMapper;

	@Resource
	private XfuActivityMapper xfuActivityMapper;
	
	@Resource
	private DataDictionaryManagementService dd;
	
	@Resource 
	private XfuActivityEntryMapper xfuActivityEntryMapper;
	
	@Resource 
	private XfuActivityTicketMapper XfuActivityTicketMapper;
	
	@Resource
	private PublicService  publicService;
	
	@Resource 
	private XfuUploadImgMapper xfuUploadImgMapper;

	@Resource
	private XfuActivityCommentMapper xfuActivityCommentMapper;
	
	@Resource 
	private XfuActivityLotteryMapper xfuActivityLotteryMapper;
	
	@Resource 
	private XfuUserMapper xfuUserMapper;
	
	@Override
	public PageResultSet getMyActivityList(MyActivityListQuery query) {
		/*
		 * if (!BaseUtils.isEmpty(query.getOrderBy())) {
		 * PageHelper.orderBy(query.getOrderBy()); }
		 */
		Page<XfuActivity> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),true)
				.doSelectPage(() -> xfuActivityMapper.selectMyActivityList(query));
		
		PageResultSet<MyActivityListDto> resultSet = new PageResultSet<>();
		// 根据需要封装Dto
		List<MyActivityListDto> listDto = new ArrayList<>();
		for (XfuActivity object : page.getResult()) {
			listDto.add(new MyActivityListDto(object));
		}
		resultSet.setRows(listDto);
		// resultSet.setRows(page); 等同上行
		resultSet.setTotal(page.getTotal());
		//
		if(query.getOffset() == 0) {
			resultSet.getDataDictMap().put("activityOrderDict", dd.findByDictType("xfuActivityOrderDict"));
			resultSet.getDataDictMap().put("activityApplyDict", dd.findByDictType("xfuActivityApplyDict"));			
		}
		
		return resultSet;
	}
	
	// 提审，前端的前提：编辑+完整（题图+票种）+未过期，此处也要判断。
		//  此处自动审核通过，未来考虑人工审查。
		@Override
		public Boolean updateActivityByUp(MyActivityListQuery query) {

			// 找到老的活动内容
			Weekend<XfuActivity> example = Weekend.of(XfuActivity.class);
			WeekendCriteria<XfuActivity, Object> criteria = example.weekendCriteria();
			criteria.andEqualTo(XfuActivity::getXfuActivityId, query.getActivityId());
			criteria.andEqualTo(XfuActivity::getXfuUserId, query.getUserId());  //必要条件
			XfuActivity oldActivity = xfuActivityMapper.selectOneByExample(example);
			
			// 活动合规判断 
			if(BaseUtils.isNotNull(oldActivity) && // 存在
					oldActivity.getXfuActivityEnd().after(new Date()) //&& //未过期,
					// oldActivity.getXfuActivityApplyDict().equals(XfuActivityApplyDict.EDIT.getValue()) //公开编辑状态,或私有转为公开
					){
				// 合规
			}else {
				return false; //不合规
			}
			
			// 票种合规判断 存在启用的票种
			List<XfuActivityTicket> tickets = XfuActivityTicketMapper.selectOKByActivityId(query.getActivityId());
			if(tickets.size()==0) {
				return false; //不合规
			}
			
			//开始核心逻辑，找到临时表用表
			
			Weekend<XfuActivityTemp> ee = Weekend.of(XfuActivityTemp.class);
			WeekendCriteria<XfuActivityTemp, Object> cc = ee.weekendCriteria();
			cc.andEqualTo(XfuActivityTemp::getXfuActivityId, query.getActivityId());
			cc.andEqualTo(XfuActivityTemp::getXfuUserId, query.getUserId());
			XfuActivityTemp temp = xfuActivityTempMapper.selectOneByExample(ee);
			
			XfuActivity  activity = new XfuActivity(temp);
			activity.setXfuActivityOrderDict(XfuActivityOrderDict.UP.getValue());
			activity.setXfuActivityApplyDict(XfuActivityApplyDict.SUCCESS.getValue());
			xfuActivityMapper.updateByPrimaryKeySelective(activity);
			
			//标注已经失效的内容图片
			Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
			WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
			c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.ACTIVITYPIC.getValue());//活动的图片
			c.andEqualTo(XfuUploadImg::getXfuUploadFunId,activity.getXfuActivityId());
			c.andEqualTo(XfuUploadImg::getXfuUploadUsed,1);//使用中的			
			List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
			for (XfuUploadImg xfuUploadImg : imageList) {
				if(activity.getXfuActivityContent().contains(xfuUploadImg.getXfuUploadSrc()) || 
						activity.getXfuActivityPic().contains(xfuUploadImg.getXfuUploadSrc())) { 
					//还在使用中呢
				}else {			
					xfuUploadImg.setXfuUploadUsed(2);
					xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
				}
			}

			return true;
		}
		
		@Override
		public Result saveActivityLotterySettings(MyActivityLotteryVo vo) {
			
			XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(vo.getActivityId());
			if (BaseUtils.isNull(activity)) return Result.failure(ResultCodeEnum.FAILD);
			if(!activity.getXfuUserId().equals(vo.getUserId())) return Result.failure(ResultCodeEnum.FAILD);
			
			XfuActivity result = new XfuActivity();
			result.setXfuActivityId(vo.getActivityId());
			result.setXfuLotterySettings(vo.getSettings());
			xfuActivityMapper.updateByPrimaryKeySelective(result);
			
			//处理已经不再使用的图片
			if(BaseUtils.isNotEmpty(vo.getSettings())) {
				//xfuActivity //  addUpdate
				Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
				WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
				c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.LOTTERYPIC.getValue());
				c.andEqualTo(XfuUploadImg::getXfuUploadFunId,vo.getActivityId());
				List<Integer> para = new ArrayList<>();
				para.add(0);
				para.add(1);
				c.andIn(XfuUploadImg::getXfuUploadUsed, para);//使用中的，待确认	
				List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
				for (XfuUploadImg xfuUploadImg : imageList) {
					if(vo.getSettings().contains(xfuUploadImg.getXfuUploadSrc())) { //还在使用中呢
						xfuUploadImg.setXfuUploadUsed(1);
						xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
					}else {
						xfuUploadImg.setXfuUploadUsed(2);
						xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
					}
				}
			}			
			return Result.success();
		}
		
		@Override
		public MyActivityLotteryDto getActivityLotterySettings(MyActivityLotteryQuery query) {
			Weekend<XfuActivity> example = Weekend.of(XfuActivity.class);
			WeekendCriteria<XfuActivity, Object> criteria = example.weekendCriteria();
			criteria.andEqualTo(XfuActivity::getXfuActivityId, query.getActivityId());
			criteria.andEqualTo(XfuActivity::getXfuUserId, query.getUserId());
			return new MyActivityLotteryDto(xfuActivityMapper.selectOneByExample(example));
		}
		
		@Override
		public Result addActivityLotteryResult(LotteryResultVo vo) {
			XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(vo.getActivityId());
			if (BaseUtils.isNull(activity)) return Result.failure(ResultCodeEnum.FAILD);
			if(!activity.getXfuUserId().equals(vo.getUserId())) return Result.failure(ResultCodeEnum.FAILD);
			
			xfuActivityLotteryMapper.insertSelective(vo.convert());
			
			return Result.success();
		}
		
		// 并没有采用 resutlSet 来处理,只有在需要total 或有数据字典时，才使用。
		@Override
		public List<LotteryResultDto> getActivityLotteryResults(LotteryQuery query){
			List<LotteryResultDto> listDto = new ArrayList<LotteryResultDto>();
			XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(query.getActivityId());
			if(BaseUtils.isNull(activity) || !activity.getXfuUserId().equals(query.getUserId())) return listDto;
			
			Weekend<XfuActivityLottery> example = Weekend.of(XfuActivityLottery.class);
			WeekendCriteria<XfuActivityLottery, Object> criteria = example.weekendCriteria();
			criteria.andEqualTo(XfuActivityLottery::getXfuActivityId, query.getActivityId());
			example.setOrderByClause("xfu_lottery_id desc");
			Page<XfuActivityLottery> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
					.doSelectPage(() -> xfuActivityLotteryMapper.selectByExample(example)); 
			
			for (XfuActivityLottery Lottery : page.getResult()) {
				LotteryResultDto dto = new LotteryResultDto(Lottery);
				JSONArray array = JSONObject.parseArray(dto.getResult());
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < array.size(); i++) {  
					JSONObject o = array.getJSONObject(i);
					buf.append(o.get("showStr"));
					buf.append(",");
				}
				dto.setResult(buf.toString());
				listDto.add(dto);
			}
			return listDto;
		}
		
		@Override
		public void dlActivityLotteryResults(LotteryQuery query,HttpServletResponse httpServletResponse){

			XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(query.getActivityId());
			if(BaseUtils.isNull(activity) || !activity.getXfuUserId().equals(query.getUserId())) return ;
			
			Weekend<XfuActivityLottery> example = Weekend.of(XfuActivityLottery.class);
			WeekendCriteria<XfuActivityLottery, Object> criteria = example.weekendCriteria();
			criteria.andEqualTo(XfuActivityLottery::getXfuActivityId, query.getActivityId());
			example.setOrderByClause("xfu_lottery_id desc");
			List<XfuActivityLottery> list = xfuActivityLotteryMapper.selectByExample(example); 			
			// 组织表头
			List<List<String>> headList = new ArrayList<List<String>>();
			List<String> head = new ArrayList<String>();
			head.add("抽取时间");
			headList.add(head);
			head = new ArrayList<String>();
			head.add("抽取批次");
			headList.add(head);
			head = new ArrayList<String>();
			head.add("抽取结果");
			headList.add(head);
			
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );		
			List<List<String>> rowList = new ArrayList<List<String>>();
			for (XfuActivityLottery lottery : list) {
				List<String> row = new ArrayList<String>();
				row.add(sdf.format(lottery.getXfuLotteryCreate()));
				row.add(lottery.getXfuLotteryRemark());
				
				JSONArray array = JSONObject.parseArray(lottery.getXfuLotteryResult());
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < array.size(); i++) {  
					JSONObject o = array.getJSONObject(i);
					buf.append(o.get("showStr"));
					buf.append(",");
				}
				row.add(buf.toString());
				
				rowList.add(row);
			}
			// 头的策略
	        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
	        // 背景设置为红色
	        headWriteCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
	        WriteFont headWriteFont = new WriteFont();
	        headWriteFont.setFontHeightInPoints((short)10);
	        headWriteFont.setColor(IndexedColors.WHITE.getIndex());
	        headWriteCellStyle.setWriteFont(headWriteFont);
	        // 内容的策略
	        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
	        
	        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
	                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
	    		
	        httpServletResponse.setContentType("application/vnd.ms-excel");
	        httpServletResponse.setCharacterEncoding("utf-8");
	        httpServletResponse.setHeader("Content-disposition", "attachment;filename=kelvin.xlsx");		
	            try {
	    			EasyExcel.write(httpServletResponse.getOutputStream()).registerWriteHandler(horizontalCellStyleStrategy)
	    			    // 这里放入动态头
	    			    .head(headList).sheet("抽奖列表")
	    			    // 当然这里数据也可以用 List<List<String>> 去传入
	    			    .doWrite(rowList );
	    		} catch (IOException e) {
	    			
	    			e.printStackTrace();
	    		}
			
		}
		
		@Override
		public Result updateActivityByDown(MyActivityListQuery query) {
			// 变成私有的了，所以如果之前有更新的，也可以直接更新过来。
			
			XfuActivityTemp old = xfuActivityTempMapper.selectByPrimaryKey(query.getActivityId());
			if (BaseUtils.isNull(old)) return Result.failure(ResultCodeEnum.FAILD);
			if(!old.getXfuUserId().equals(query.getUserId())) return Result.failure(ResultCodeEnum.FAILD);

			XfuActivity  activity = new XfuActivity(old);
			//activity.setXfuActivityId(query.getActivityId());
			//activity.setXfuUserId(query.getUserId());
			activity.setXfuActivityOrderDict(XfuActivityOrderDict.DOWN.getValue());
			activity.setXfuActivityApplyDict(XfuActivityApplyDict.SUCCESS.getValue());
			xfuActivityMapper.updateByPrimaryKeySelective(activity);
			
			
			//标注已经失效的内容图片
			Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
			WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
			c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.ACTIVITYPIC.getValue());//活动的图片
			c.andEqualTo(XfuUploadImg::getXfuUploadFunId,activity.getXfuActivityId());
			c.andEqualTo(XfuUploadImg::getXfuUploadUsed,1);//使用中的			
			List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
			for (XfuUploadImg xfuUploadImg : imageList) {
				if(activity.getXfuActivityContent().contains(xfuUploadImg.getXfuUploadSrc()) || 
						activity.getXfuActivityPic().contains(xfuUploadImg.getXfuUploadSrc())) { 
					//还在使用中呢
				}else {			
					xfuUploadImg.setXfuUploadUsed(2);
					xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
				}
			}
			
			return Result.success();
		}
		
	
		@Override
		public String savePic(PicFileQuery query) throws IllegalStateException, IOException {
			
			//原则上需要判断活动ID是用户的，但此处省略，因为图片针对2 和超过1天的0是要集中处理的。
			
			MultipartFile file = query.getFile();
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
			img.setXfuUploadFunType(query.getFunType());  //1,活动的图片  3,活动抽奖背景图,5,签到墙底图
			img.setXfuUploadFunId(query.getActivityId());
			img.setXfuUploadUsed(0); // 0待定，1使用，2未使用
			img.setXfuUploadSrc(fileName);
			xfuUploadImgMapper.insertSelective(img);
			return fileName;
		}

	
	@Transactional
	@Override
	public Result createUpdateActivity(XfuActivityTemp addUpdate) {
		if (BaseUtils.isNull(addUpdate.getXfuActivityId())) { // Add
			// 先插入主表，获取id
			XfuActivity activity = new XfuActivity(addUpdate);
			activity.setXfuActivityToken(UUID.randomUUID().toString());
			xfuActivityMapper.insertSelective(activity);
			addUpdate.setXfuActivityId(activity.getXfuActivityId());
			xfuActivityTempMapper.insertSelective(addUpdate);
			
			//插入无票种的默认票种
			XfuActivityTicket ticket = new XfuActivityTicket();
			ticket.setXfuActivityId(activity.getXfuActivityId());
			ticket.setXfuTicketName("无票种");
			ticket.setXfuTicketRemark("系统票种，用于导入使用");
			XfuActivityTicketMapper.insertSelective(ticket);
			
		} else { // Update
			Weekend<XfuActivity> example = Weekend.of(XfuActivity.class);
			WeekendCriteria<XfuActivity, Object> criteria = example.weekendCriteria();
			criteria.andEqualTo(XfuActivity::getXfuActivityId, addUpdate.getXfuActivityId());
			criteria.andEqualTo(XfuActivity::getXfuUserId, addUpdate.getXfuUserId());
			XfuActivity xfuActivity = xfuActivityMapper.selectOneByExample(example);
			if(BaseUtils.isNotNull(xfuActivity) && xfuActivity.getXfuActivityEnd().after(new Date())){
				// 未过期
			}else {
				return Result.failure(ResultCodeEnum.FAILD,"过期不能编辑");
			}
			
			xfuActivityTempMapper.updateByPrimaryKeySelective(addUpdate);
			
			if(xfuActivity.getXfuActivityOrderDict().equals(XfuActivityOrderDict.DOWN.getValue())) {//  如果是私有活动，直接更新正式表,且成功
				xfuActivity = new XfuActivity(addUpdate);
				xfuActivity.setXfuActivityApplyDict(XfuActivityApplyDict.SUCCESS.getValue());
				xfuActivityMapper.updateByPrimaryKeySelective(xfuActivity);
			}else { // 设置审核状态 为编辑中
				XfuActivity activity = new XfuActivity();
				activity.setXfuActivityId(addUpdate.getXfuActivityId());
				activity.setXfuActivityApplyDict(XfuActivityApplyDict.EDIT.getValue());
				xfuActivityMapper.updateByPrimaryKeySelective(activity);
			}

			//处理已经不再使用的图片
			if(BaseUtils.isNotEmpty(addUpdate.getXfuActivityContent())) {
				//xfuActivity //  addUpdate
				Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
				WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
				c.andEqualTo(XfuUploadImg::getXfuUploadFunType, FileType.ACTIVITYPIC.getValue());//活动的图片
				c.andEqualTo(XfuUploadImg::getXfuUploadFunId,xfuActivity.getXfuActivityId());
				List<Integer> para = new ArrayList<>();
				para.add(0);
				para.add(1);
				c.andIn(XfuUploadImg::getXfuUploadUsed, para);//使用中的，待确认	
				List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
				for (XfuUploadImg xfuUploadImg : imageList) {
					//新建记录这两个字段是空的
					if((BaseUtils.isNotEmpty(xfuActivity.getXfuActivityContent()) && xfuActivity.getXfuActivityContent().contains(xfuUploadImg.getXfuUploadSrc())) || 
						(BaseUtils.isNotEmpty(xfuActivity.getXfuActivityPic()) && xfuActivity.getXfuActivityPic().contains(xfuUploadImg.getXfuUploadSrc())) || 
							addUpdate.getXfuActivityContent().contains(xfuUploadImg.getXfuUploadSrc()) || 
							addUpdate.getXfuActivityPic().contains(xfuUploadImg.getXfuUploadSrc()) 
							) { //还在使用中呢
						xfuUploadImg.setXfuUploadUsed(1);
						xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
					}else {
						xfuUploadImg.setXfuUploadUsed(2);
						xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
					}
				}
			}
		}
		return Result.success(addUpdate.getXfuActivityId()); 
	}

	
	@Override
	public MyActivityTempPojo selectActivityTemp(MyActivityTempQuery query) {
		Weekend<XfuActivityTemp> example = Weekend.of(XfuActivityTemp.class);
		WeekendCriteria<XfuActivityTemp, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityTemp::getXfuActivityId, query.getActivityId());
		criteria.andEqualTo(XfuActivityTemp::getXfuUserId, query.getUserId());
		return new MyActivityTempPojo(xfuActivityTempMapper.selectOneByExample(example));
	}
	
	@Override
	public Result cancelEntry(MyEntryQuery query) { 
		
		List<XfuActivityEntry> entries = xfuActivityEntryMapper.selectMyEntries(query);
		
		if(entries.size()!=1)
			return Result.failure(ResultCodeEnum.FAILD);
		else {
			XfuActivityEntry entry = entries.get(0);
			//现在时间相较活动开始时间差 正2小时，可以取消。不考虑上下架等因素
			if(entry.getActivity().getXfuActivityStart().getTime()-new Date().getTime()> 2 *60 * 60 * 1000) {
				entry.setXfuCancel(new Date());
				xfuActivityEntryMapper.updateByPrimaryKeySelective(entry);
				return Result.success();
			}
			else {
				return Result.failure(ResultCodeEnum.FAILD,"活动仅允许提前2小时取消报名，请准时出席");
			}
		}

	}
	
	
	@Override
	@Transactional
	public Result addEntry(XfuActivityEntry entry) {
		
		PublicActivityQuery query = new PublicActivityQuery();
		query.setActivityId(entry.getXfuActivityId());
		PublicActivityDto activity = publicService.getPublicActity(query);//dto肯定是不NULL的，但内部属性可能是null
		
		PublicActivityTicketDto justTicket=null;
		if(BaseUtils.isNull(activity.getActivityId())) return Result.failure(ResultCodeEnum.PARAM_ERROR);
		else {
			Boolean notExpried = activity.getEntryEnd().after(new Date());
			Boolean hasTicket = false;
			for (PublicActivityTicketDto ticket : activity.getTickets()) {
				if(ticket.getTicketId().equals(entry.getXfuTicketId())) {
					if(ticket.getTicketSum()-ticket.getTicketSold()>0) {
						hasTicket = true;
						justTicket = ticket;
					}
					break;
				}
			}
			if( notExpried && hasTicket) {
				// gohead
			}else {
				return Result.failure(ResultCodeEnum.PARAM_ERROR);
			}
		}
		
		Weekend<XfuActivityEntry> example = Weekend.of(XfuActivityEntry.class);
		WeekendCriteria<XfuActivityEntry, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityEntry::getXfuUserId, entry.getXfuUserId());
		criteria.andEqualTo(XfuActivityEntry::getXfuActivityId, entry.getXfuActivityId());
		criteria.andIsNull(XfuActivityEntry::getXfuCancel);  //取消后，允许再次报名，TODO 以后考虑限制，如只能半小时后才能报名。
		XfuActivityEntry exist = xfuActivityEntryMapper.selectOneByExample(example);
		if(BaseUtils.isNull(exist)) {
			XfuActivityTicket updateTicket = new XfuActivityTicket();
			updateTicket.setXfuTicketId(justTicket.getTicketId());
			updateTicket.setXfuTicketSold(justTicket.getTicketSold()+1);
			XfuActivityTicketMapper.updateByPrimaryKeySelective(updateTicket);
			entry.setXfuCheckinCode(UUID.randomUUID().toString());
			xfuActivityEntryMapper.insertSelective(entry);
			return Result.success();
		}
		else
			return Result.failure(ResultCodeEnum.FAILD,"您已经报名，不能重复报名。");
	}
	
	
	
	@Override
	public PageResultSet getMyEntries(MyEntryQuery query) {
		Page<XfuActivityEntry> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityEntryMapper.selectMyEntries(query));
		List<MyEntryDto> listDto = new ArrayList<>();
		for (XfuActivityEntry xfuActivityEntry : page.getResult()) {
			listDto.add(new MyEntryDto(xfuActivityEntry));
		}
		PageResultSet<MyEntryDto> resultSet = new PageResultSet<>();
		resultSet.setRows(listDto);
		return resultSet;
	}
	
	@Override
	public Result getMyActivityEntries(ActivityEntryQuery query) {

		XfuActivity activity = xfuActivityMapper.selectMyActivity(query.getActivityId());
		if(BaseUtils.isNull(activity) || !activity.getXfuUserId().equals(query.getUserId())) return Result.failure(ResultCodeEnum.FAILD);
		
		Page<XfuActivityEntry> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityEntryMapper.selectActivityEntries(query));
		
		List<ActivityEntryDto> listDto = new ArrayList<>();
		for (XfuActivityEntry xfuActivityEntry : page.getResult()) {
			listDto.add(new ActivityEntryDto(xfuActivityEntry));
		}
		
		PageResultSet<ActivityEntryDto> resultSet = new PageResultSet<>();
		resultSet.setRows(listDto);		
		resultSet.setTotal(page.getTotal());
		
		
		if(query.getOffset() == 0) {
			resultSet.getDataDictMap().put("activity", new MyActivityDto(activity));
		}
		return Result.success(resultSet);
	}
	
	@Override
	public Result importActivityEntries(MyEntryVo query) throws IllegalStateException, IOException {
		XfuActivity activity = xfuActivityMapper.selectMyActivity(query.getActivityId());
		if(BaseUtils.isNull(activity) || !activity.getXfuUserId().equals(query.getUserId())) return Result.failure(ResultCodeEnum.FAILD);
		// 这里的userId并不报名用户，导入的报名是没有userId
		query.setUserId(null);
		
		MultipartFile file = query.getFile();
		
		String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String fileName = UUID.randomUUID() + suffixName;
		fileName = localActivityTemp+ "/" + fileName;
		File dest = new File(fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		file.transferTo(dest);
		
		
		EasyExcel.read(fileName, new NoModleDataListener(xfuActivityEntryMapper,query,XfuActivityTicketMapper)).sheet().doRead();
		
		
		return Result.success();
	}
	
	
	
	@Override
	public void dlMyActivityEntriesTemple(ActivityEntryQuery query,HttpServletResponse response) {
		XfuActivity activity = xfuActivityMapper.selectMyActivity(query.getActivityId());
		if(BaseUtils.isNull(activity) ||  !activity.getXfuUserId().equals(query.getUserId())) return;
		
		// List<XfuActivityEntry> entryList = xfuActivityEntryMapper.selectActivityEntries(query);
		// 组织表头
		List<List<String>> headList = new ArrayList<List<String>>();
		JSONArray questionArray = JSONObject.parseArray(activity.getForm().getXfuFormJson());
		for (int i = 0; i < questionArray.size(); i++) {  
			JSONObject jo = questionArray.getJSONObject(i); 
			List<String> head = new ArrayList<String>();			
			head.add(jo.getString("label"));
			head.add(jo.getString("key"));
			headList.add(head);
		}
		List<List<String>> rowList = new ArrayList<List<String>>();
		// 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)10);
        headWriteFont.setColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
 
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
            new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
		
		response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=kelvin.xlsx");		
        try {
			EasyExcel.write(response.getOutputStream()).registerWriteHandler(horizontalCellStyleStrategy)
			    // 这里放入动态头
			    .head(headList).sheet("导入模板")
			    // 当然这里数据也可以用 List<List<String>> 去传入
			    .doWrite(rowList );
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public Result getMyActivityAllEntries(ActivityEntryQuery query){
		XfuActivity activity = xfuActivityMapper.selectMyActivity(query.getActivityId());
		if(BaseUtils.isNull(activity) || !activity.getXfuUserId().equals(query.getUserId())) return Result.failure(ResultCodeEnum.FAILD);
		List<XfuActivityEntry> entryList = xfuActivityEntryMapper.selectActivityEntries(query);	
		
		JSONArray questionArray = JSONObject.parseArray(activity.getForm().getXfuFormJson());
		String key = questionArray.getJSONObject(0).getString("key");  //  name_1
		List<MyActivityEntryForLottery> list = new ArrayList<>();
		for (XfuActivityEntry entry : entryList) {
			MyActivityEntryForLottery lottery = new MyActivityEntryForLottery();
			JSONObject anwser = JSONObject.parseObject(entry.getXfuEntryContent());// 一条报名信息，包括若干答案
			lottery.setEntryId(entry.getXfuEntryId());
			lottery.setShowStr(anwser.getString(key));
			list.add(lottery);
		}
		return Result.success(list);
	}
	
	@Override
	public void dlMyActivityEntries(ActivityEntryQuery query,HttpServletResponse response) {

		XfuActivity activity = xfuActivityMapper.selectMyActivity(query.getActivityId());
		if(BaseUtils.isNull(activity) || !activity.getXfuUserId().equals(query.getUserId())) return;
		
		List<XfuActivityEntry> entryList = xfuActivityEntryMapper.selectActivityEntries(query);
		// 组织表头
		List<List<String>> headList = new ArrayList<List<String>>();
		JSONArray questionArray = JSONObject.parseArray(activity.getForm().getXfuFormJson());
		for (int i = 0; i < questionArray.size(); i++) {  
			JSONObject jo = questionArray.getJSONObject(i); 
			List<String> head = new ArrayList<String>();
			// head.add(jo.getString("key"));
			head.add(jo.getString("label"));
			headList.add(head);
		}
		
		List<String> head = new ArrayList<String>();
		head.add("报名时间");
		headList.add(head);
		head = new ArrayList<String>();
		head.add("票种");
		headList.add(head);
		head = new ArrayList<String>();
		head.add("签到时间");
		headList.add(head);
		head = new ArrayList<String>();
		head.add("取消时间");
		headList.add(head);
		
		// 还原报名信息
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );		
		List<List<String>> rowList = new ArrayList<List<String>>();
		for (XfuActivityEntry entry : entryList) {
			List<String> row = new ArrayList<String>();
			JSONObject anwser = JSONObject.parseObject(entry.getXfuEntryContent());// 一条报名信息，包括若干答案
			for (int i = 0; i < questionArray.size(); i++) {  
				JSONObject jo = questionArray.getJSONObject(i); //每个题目
				switch (jo.getString("controlType")) {
				case "textbox":
				case "textmorebox":
					row.add(anwser.getString(jo.getString("key"))); // 找到题目对应的答案
					break;
				default:
					JSONArray optionArray = jo.getJSONArray("options");	// 答案转义的对应关系				
					// 分拆答案
					String trans="";
					String temp = anwser.getString(jo.getString("key"));//找到题目对应的答案
					//中途更改表单问题导致为空的情况
					if(BaseUtils.isNotEmpty(temp)) { //if内，针对答案进行转义
						temp = temp.replaceAll("\\[", "").replaceAll("\\]", "");
						String tempGroup [] = temp.split(",");											
						// 每个答案 寻找对应的翻译值
						for (int j = 0; j < tempGroup.length; j++) {
							Boolean find=false;
							for (int k = 0; k < optionArray.size(); k++) {
								JSONObject option = optionArray.getJSONObject(k);
								if( tempGroup[j].equals(option.getString("key"))) {
									trans = trans +option.getString("value")+",";
									find=true;
									break;
								}
							}
							if(!find) { // 如果转义不成功，就直接用没有转意前的答案
								trans = trans +tempGroup[j]+",";
							}
						}
					}
					if(trans.length()>0) trans= trans.substring(0, trans.length()-1);
					row.add(trans);
					
					break;
				}
				
			}
			
			// 报名时间				
			row.add(sdf.format(entry.getXfuEntryCreate()));
			
			//报名票种
			for (XfuActivityTicket ticket : activity.getTickets()) {
				if(entry.getXfuTicketId().equals(ticket.getXfuTicketId())) {
					row.add(ticket.getXfuTicketName());
					break;
				}
			}
			//签到时间，取消时间
			if(BaseUtils.isNotNull(entry.getXfuCheckin())) row.add(sdf.format(entry.getXfuCheckin()));
			else row.add("");
			if(BaseUtils.isNotNull(entry.getXfuCancel())) row.add(sdf.format(entry.getXfuCancel()));
			else row.add("");
			//该行准备好了
			rowList.add(row);
		}
		
		// 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)10);
        headWriteFont.setColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        //contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        //contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        //WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        //contentWriteFont.setFontHeightInPoints((short)20);
        //contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
            new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
		
		response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=kelvin.xlsx");		
        try {
			EasyExcel.write(response.getOutputStream()).registerWriteHandler(horizontalCellStyleStrategy)
			    // 这里放入动态头
			    .head(headList).sheet("报名列表")
			    // 当然这里数据也可以用 List<List<String>> 去传入
			    .doWrite(rowList );
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	//出示个人签到码，主办方登录来检查信息
	@Override
	public Result signByEntryCheckinCode(ActivityEntryQuery query) {
		XfuActivityEntry entry = xfuActivityEntryMapper.selectByPrimaryKey(query.getId()); //找到用户的报名信息
		if(BaseUtils.isNotNull(entry)) {
			
			if(BaseUtils.isNotNull(entry.getXfuCancel())) return Result.failure(ResultCodeEnum.FAILD,"报名取消，不能签到"); //标识不一致
			
			if(!query.getCode().contentEquals(entry.getXfuCheckinCode())) return Result.failure(ResultCodeEnum.FAILD,"校验失败，请核实二维码"); //标识不一致
			
			XfuActivity activity = xfuActivityMapper.selectMyActivity(entry.getXfuActivityId());
			
			if(BaseUtils.isNull(activity))  return Result.failure(ResultCodeEnum.FAILD);
			if(!activity.getXfuUserId().equals(query.getUserId())) return Result.failure(ResultCodeEnum.FAILD,"校验失败，请核实二维码"); // 活动不是我主办方的
			
			if(BaseUtils.isNotNull(entry.getXfuCheckin())) 
				return Result.failure(ResultCodeEnum.FAILD,"<p>已经签到，不能重复签到</p>" + this.reformUserEntry(activity, entry));
			if(DateUtil.addDay(activity.getXfuActivityEnd(), 1).before(new Date())) 
				return Result.failure(ResultCodeEnum.FAILD,"<p>活动已经结束24小时，抱歉不能签到了</p>" + this.reformUserEntry(activity, entry));
			//通过了，可以签到了
			entry.setXfuCheckin(new Date());
			xfuActivityEntryMapper.updateByPrimaryKeySelective(entry);
			
			//send message  
			String head="";
			if(BaseUtils.isNotNull(entry.getXfuUserId())) {
				XfuUser user = xfuUserMapper.selectByPrimaryKey(entry.getXfuUserId());
				if(BaseUtils.isNotEmpty(user.getXfuUserAvatar())) {
					head = user.getXfuUserAvatar();
				}			
			}
			JSONObject anwser = JSONObject.parseObject(entry.getXfuEntryContent());
			this.myHandler.sendMessageToUser(activity.getXfuActivityId().toString(), new TextMessage("{\"name\":\"" + anwser.getString("name_1") 
				+ "\",\"head\":\""+ head +"\"}") );
			
			return Result.success("<p>签到成功</p>" + this.reformUserEntry(activity, entry));
			
		}else {
			return Result.failure(ResultCodeEnum.FAILD,"校验失败，请核实二维码");
		}
		
	}
	
	//参会者登录 ，扫描 活动现场码
	@Override
	public Result singByActivityToken(MyEntryQuery query) {
		
		Weekend<XfuActivityEntry> example = Weekend.of(XfuActivityEntry.class);
		WeekendCriteria<XfuActivityEntry, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityEntry::getXfuActivityId,query.getId());
		criteria.andEqualTo(XfuActivityEntry::getXfuUserId,query.getUserId());
		criteria.andIsNull(XfuActivityEntry::getXfuCancel);// normal is not cancel
		XfuActivityEntry entry = xfuActivityEntryMapper.selectOneByExample(example);
		if(BaseUtils.isNotNull(entry)) {
			
			XfuActivity activity = xfuActivityMapper.selectMyActivity(entry.getXfuActivityId());

			if(BaseUtils.isNull(activity))  return Result.failure(ResultCodeEnum.FAILD);			
			if(!query.getCode().equals(activity.getXfuActivityToken())) return Result.failure(ResultCodeEnum.FAILD,"校验失败，请核实二维码");		
			
			if(BaseUtils.isNotNull(entry.getXfuCheckin())) 
				return Result.failure(ResultCodeEnum.FAILD,"<p>已经签到，不能重复签到</p>" + this.reformUserEntry(activity, entry));
			if(DateUtil.addDay(activity.getXfuActivityEnd(), 1).before(new Date())) 
				return Result.failure(ResultCodeEnum.FAILD,"<p>活动已经结束24小时，抱歉不能签到了</p>" + this.reformUserEntry(activity, entry));
			//通过了，可以签到了
			entry.setXfuCheckin(new Date());
			xfuActivityEntryMapper.updateByPrimaryKeySelective(entry);
			
			String head="";
			if(BaseUtils.isNotNull(entry.getXfuUserId())) {
				XfuUser user = xfuUserMapper.selectByPrimaryKey(entry.getXfuUserId());
				if(BaseUtils.isNotEmpty(user.getXfuUserAvatar())) {
					head = user.getXfuUserAvatar();
				}			
			}
			//send message  
			JSONObject anwser = JSONObject.parseObject(entry.getXfuEntryContent());
			this.myHandler.sendMessageToUser(activity.getXfuActivityId().toString(), new TextMessage("{\"name\":\"" + anwser.getString("name_1") 
				+ "\",\"head\":\""+ head +"\"}") );
			
			return Result.success("<p>签到成功</p>" + this.reformUserEntry(activity, entry));
			
		}else {
			return Result.failure(ResultCodeEnum.FAILD,"校验失败，请核实二维码");
		}
	}
	
	
	private String reformUserEntry(XfuActivity activity,XfuActivityEntry entry) {
		List<String> headList = new ArrayList<String>();
		JSONArray questionArray = JSONObject.parseArray(activity.getForm().getXfuFormJson());
		for (int i = 0; i < questionArray.size(); i++) {  
			JSONObject jo = questionArray.getJSONObject(i); 
			headList.add(jo.getString("label"));
		}
		List<String> rowList = new ArrayList<String>();
		
		JSONObject anwser = JSONObject.parseObject(entry.getXfuEntryContent());
		
		for (int i = 0; i < questionArray.size(); i++) {  
			JSONObject jo = questionArray.getJSONObject(i); 
			switch (jo.getString("controlType")) {
			case "textbox":
			case "textmorebox":
				rowList.add(anwser.getString(jo.getString("key")));
				break;
			default:
				JSONArray optionArray = jo.getJSONArray("options");
				
				// 分拆答案
				String trans="";
				String temp = anwser.getString(jo.getString("key"));
				//中途更改表单问题导致为空的情况
				if(BaseUtils.isNotEmpty(temp)) {
					temp = temp.replaceAll("\\[", "").replaceAll("\\]", "");
					String tempGroup [] = temp.split(",");
					
					
					// 每个答案 寻找对应的翻译值
					for (int j = 0; j < tempGroup.length; j++) {
						for (int k = 0; k < optionArray.size(); k++) {
							JSONObject option = optionArray.getJSONObject(k);
							if( tempGroup[j].equals(option.getString("key"))) {
								trans = trans +option.getString("value")+",";
								break;
							}
						}
					}
				}
				if(trans.length()>0) trans= trans.substring(0, trans.length()-1);
				rowList.add(trans);				
				break;
			}
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("<p>");
		for(int i=0; i< headList.size(); i++) {
			buf.append(headList.get(i) + ":" + rowList.get(i) + "<br>");
		}
		buf.append("</p>");
		return buf.toString();
		
	}
	
	// 代签到
	@Override
	public Result signBySponsor(ActivityEntryQuery query) {
		Weekend<XfuActivityEntry> example = Weekend.of(XfuActivityEntry.class);
		WeekendCriteria<XfuActivityEntry, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityEntry::getXfuEntryId,query.getEntryId());
		criteria.andIsNull(XfuActivityEntry::getXfuCancel);
		XfuActivityEntry entry = xfuActivityEntryMapper.selectOneByExample(example);
		if(BaseUtils.isNotNull(entry)) {
			if(BaseUtils.isNotNull(entry.getXfuCheckin())) return Result.failure(ResultCodeEnum.FAILD,"已经签到，不能重复签到");
			XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(entry.getXfuActivityId());
			if(BaseUtils.isNull(activity))  return Result.failure(ResultCodeEnum.FAILD);
			if(DateUtil.addDay(activity.getXfuActivityEnd(), 1).before(new Date())) return Result.failure(ResultCodeEnum.FAILD,"活动已经结束24小时，抱歉不能再签到");
			if(!activity.getXfuUserId().equals(query.getUserId())) return Result.failure(ResultCodeEnum.FAILD); // 活动不是我主办方的
			//通过了，可以签到了
			entry.setXfuCheckin(new Date());
			xfuActivityEntryMapper.updateByPrimaryKeySelective(entry);
			
			String head="";
			if(BaseUtils.isNotNull(entry.getXfuUserId())) {
				XfuUser user = xfuUserMapper.selectByPrimaryKey(entry.getXfuUserId());
				if(BaseUtils.isNotEmpty(user.getXfuUserAvatar())) {
					head = user.getXfuUserAvatar();
				}
				
			}
			//send message  
			JSONObject anwser = JSONObject.parseObject(entry.getXfuEntryContent());
			this.myHandler.sendMessageToUser(activity.getXfuActivityId().toString(), new TextMessage("{\"name\":\"" + anwser.getString("name_1") 
				+ "\",\"head\":\""+ head +"\"}") );
			
			return Result.success();
		}else {
			return Result.failure(ResultCodeEnum.FAILD);
		}
	}
	
	// 代取消 其实是改变状态
		@Override
		public Result cancelBySponsor(ActivityEntryQuery query) {
			Weekend<XfuActivityEntry> example = Weekend.of(XfuActivityEntry.class);
			WeekendCriteria<XfuActivityEntry, Object> criteria = example.weekendCriteria();
			criteria.andEqualTo(XfuActivityEntry::getXfuEntryId,query.getEntryId());
			XfuActivityEntry entry = xfuActivityEntryMapper.selectOneByExample(example);
			if(BaseUtils.isNotNull(entry)) {
				XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(entry.getXfuActivityId());
				if(BaseUtils.isNull(activity))  return Result.failure(ResultCodeEnum.FAILD);
				if(!activity.getXfuUserId().equals(query.getUserId())) return Result.failure(ResultCodeEnum.FAILD); // 活动不是我主办方的
				//通过了，可以toggle了
				if(BaseUtils.isNull(entry.getXfuCancel())) {
					entry.setXfuCancel(new Date());
				}else {
					entry.setXfuCancel(null);
				}				
				xfuActivityEntryMapper.updateByPrimaryKey(entry);
				return Result.success();
			}else {
				return Result.failure(ResultCodeEnum.FAILD);
			}
		}
	
	@Override
	public Result createComment(XfuActivityComment comment) {
		
		// 不能连续评论
		Weekend<XfuActivityComment> example = Weekend.of(XfuActivityComment.class);
		WeekendCriteria<XfuActivityComment, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivityComment::getXfuActivityId,comment.getXfuActivityId());
		criteria.andEqualTo(XfuActivityComment::getXfuUserId,comment.getXfuUserId());
		example.setOrderByClause("xfu_comment_id desc");
		Page<XfuActivityComment> page =  PageHelper.offsetPage(0, 1,false)
			.doSelectPage(() -> xfuActivityCommentMapper.selectByExample(example));
		if(page.getResult().size()>0 && 
				page.getResult().get(0).getXfuCommentCreate().before(DateUtil.addMinute(new Date(),-60))) {
			xfuActivityCommentMapper.insertSelective(comment);
			return Result.success();
		}else if(page.getResult().size()==0) {
			xfuActivityCommentMapper.insertSelective(comment);
			return Result.success();
		}else {
			return Result.failure(ResultCodeEnum.FAILD,"一小时内仅能评论一次");
		}
	}
	
	@Override
	public Result replayComment(ActivityCommentVo vo) {
		ActivityCommentQuery query = new ActivityCommentQuery();
		query.setUserId(vo.getUserId());
		query.setCommentId(vo.getCommentId());
		if(xfuActivityCommentMapper.selectActivityComments(query).size()==1) {
			XfuActivityComment comment = vo.convert();
			comment.setXfuCommentUpdate(new Date());
			xfuActivityCommentMapper.updateByPrimaryKeySelective(comment);
			return Result.success();
		}else {
			return Result.failure(ResultCodeEnum.FAILD);
		}
	}
	
	@Override
	public PageResultSet getMyComments(MyCommentsQuery query) {
		Page<XfuActivityComment> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityCommentMapper.selectMyComments(query.getUserId()));
		
		List<CommentDto> listDto = new ArrayList<>();
		
		for (XfuActivityComment comment : page.getResult()) {
			listDto.add(new CommentDto(comment));
		}
		
		PageResultSet resultSet = new PageResultSet();
		resultSet.setRows(listDto);
		
		return resultSet;
		
		
	}
	
	@Override
	public PageResultSet getActivityComments(ActivityCommentQuery query) {
		Page<XfuActivityComment> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),false)
				.doSelectPage(() -> xfuActivityCommentMapper.selectActivityComments(query));
		
		List<CommentDto> listDto = new ArrayList<>();
		
		for (XfuActivityComment comment : page.getResult()) {
			listDto.add(new CommentDto(comment));
		}
		
		PageResultSet resultSet = new PageResultSet();
		resultSet.setRows(listDto);
		
		return resultSet;
		
		
	}

	@Override
	public Result delComment(MyCommentsQuery query) {
		XfuActivityComment exist = xfuActivityCommentMapper.selectByPrimaryKey(query.getCommentId());
		if(BaseUtils.isNotNull(exist) 
				&& exist.getXfuUserId().equals(query.getUserId())) {
			// 不建起用exits 来更新
			XfuActivityComment u = new XfuActivityComment();
			u.setXfuCommentId(query.getCommentId());
			u.setXfuCommentDel(true);
			xfuActivityCommentMapper.updateByPrimaryKeySelective(u);
			return Result.success();
		}
		return Result.failure(ResultCodeEnum.FAILD);
	}
	
	/**
	 * 暂未使用
	 */
	@Override
	public Result saveWallSettings(MyActivityWallVo vo) {
		XfuActivity activity = xfuActivityMapper.selectByPrimaryKey(vo.getActivityId());
		if (BaseUtils.isNull(activity)) return Result.failure(ResultCodeEnum.FAILD);
		if(!activity.getXfuUserId().equals(vo.getUserId())) return Result.failure(ResultCodeEnum.FAILD);
		
		XfuActivity result = new XfuActivity();
		result.setXfuActivityId(vo.getActivityId());
		result.setXfuLotterySettings(vo.getWallSettings());
		xfuActivityMapper.updateByPrimaryKeySelective(result);
		
		//处理已经不再使用的图片
		if(BaseUtils.isNotEmpty(vo.getWallSettings())) {
			//xfuActivity //  addUpdate
			Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
			WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
			c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.CHECKINPIC.getValue());
			c.andEqualTo(XfuUploadImg::getXfuUploadFunId,vo.getActivityId());
			List<Integer> para = new ArrayList<>();
			para.add(0);
			para.add(1);
			c.andIn(XfuUploadImg::getXfuUploadUsed, para);//使用中的，待确认	
			List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
			for (XfuUploadImg xfuUploadImg : imageList) {
				if(vo.getWallSettings().contains(xfuUploadImg.getXfuUploadSrc())) { //还在使用中呢
					xfuUploadImg.setXfuUploadUsed(1);
					xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
				}else {
					xfuUploadImg.setXfuUploadUsed(2);
					xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
				}
			}
		}			
		return Result.success();
	}
	@Override
	public Result getActivityWallSettingsAndTotal(MyActivityWallQuery query) {
		Weekend<XfuActivity> example = Weekend.of(XfuActivity.class);
		WeekendCriteria<XfuActivity, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuActivity::getXfuActivityId, query.getActivityId());
		criteria.andEqualTo(XfuActivity::getXfuUserId, query.getUserId());
		XfuActivity  activity = xfuActivityMapper.selectOneByExample(example);
		if (BaseUtils.isNull(activity)) return Result.failure(ResultCodeEnum.FAILD);
		
		MyActivityWallDto dto = new MyActivityWallDto();
		dto.setWallSettings(activity.getXfuWallSettings());
		
		List<XfuActivityTicket> ticketList = XfuActivityTicketMapper.selectAllByActivityId(activity.getXfuActivityId());
		int total = 0 ; 
		for(XfuActivityTicket ticket:ticketList ) {
			total = total + ticket.getXfuTicketSold();
		}
		dto.setTotal(total);
				
		ActivityEntryQuery q = new ActivityEntryQuery();
		q.setActivityId(query.getActivityId());
		q.setCheckin(true);
		List<XfuActivityEntry> entryList = xfuActivityEntryMapper.selectActivityEntries(q);
		
		List<String> names = new ArrayList<String>();
		List<String> heads = new ArrayList<String>();
		for(XfuActivityEntry entry: entryList) {
			JSONObject anwser = JSONObject.parseObject(entry.getXfuEntryContent());
			names.add(anwser.getString("name_1"));
			if(BaseUtils.isNull(entry.getUser())) {
				heads.add(null);
			} else {
				heads.add(entry.getUser().getXfuUserAvatar());
			}
			
		}
		dto.setNames(names);
		dto.setHeads(heads);
		
		return Result.success(dto);
		
	}


	// 不包括第一行哦
	public class NoModleDataListener extends AnalysisEventListener<Map<Integer, String>> {
	   
	    private static final int BATCH_COUNT = 5;
	    List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
	    
	    private XfuActivityEntryMapper xfuActivityEntryMapper;
	    private MyEntryVo query;
	    private XfuActivityTicketMapper xfuActivityTicketMapper;
	    private Integer total =0;
	    
	    Map<Integer, String> head = new HashMap<Integer, String>();
	    
	    boolean first=true;

	    public NoModleDataListener(XfuActivityEntryMapper xfuActivityEntryMapper,MyEntryVo query,XfuActivityTicketMapper xfuActivityTicketMapper) {
			this.xfuActivityEntryMapper = xfuActivityEntryMapper;
			this.xfuActivityTicketMapper = xfuActivityTicketMapper;
			this.query = query;
		}
	    
	    @Override
	    public void invoke(Map<Integer, String> data, AnalysisContext context) {
	       
	        list.add(data);
	        if (list.size() >= BATCH_COUNT) {
	            saveData();
	            list.clear();
	        }
	    }

	    @Override
	    public void doAfterAllAnalysed(AnalysisContext context) {
	        saveData();
	        
	        // 更新票种 销售数量	        
	        XfuActivityTicket ticket = xfuActivityTicketMapper.selectByPrimaryKey(query.getTicketId());
	        ticket.setXfuTicketSold(ticket.getXfuTicketSold()+total);
	        xfuActivityTicketMapper.updateByPrimaryKeySelective(ticket);
	        
	    }


	    private void saveData() {
	    	for(int i=0;i<list.size();i++) {
	    		
	    		log.debug(list.get(i).toString());

	    		if(i ==0 && first) {//记录下表单
	    			head = new HashMap<Integer, String>(list.get(i));
	    			first =false;
	    			continue;
	    		}
	    		
	    		Map<String, String> a = new HashMap<String, String>();
	    		for(Map.Entry<Integer, String> entry : head.entrySet()){
	    			 a.put(entry.getValue(), list.get(i).get(entry.getKey()));
	    		}
	    		String content = JSON.toJSONString(a);
	    		query.setEntryContent(content);	  
	    		total++;
	    		xfuActivityEntryMapper.insertSelective(query.convert());
	    	}
	    }
	}
	
}


