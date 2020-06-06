package net.xfunction.java.api.modules.questionnaire.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.xfunction.java.api.core.enums.FileType;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuUploadImgMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuUploadImg;
import net.xfunction.java.api.modules.activity.pojo.activityEntry.ActivityEntryDto;
import net.xfunction.java.api.modules.activity.pojo.myActivity.MyActivityDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListDto;
import net.xfunction.java.api.modules.questionnaire.mapper.xfunction.XfuQuestionnaireEntryMapper;
import net.xfunction.java.api.modules.questionnaire.mapper.xfunction.XfuQuestionnaireFormMapper;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireEntry;
import net.xfunction.java.api.modules.questionnaire.model.xfunction.XfuQuestionnaireForm;
import net.xfunction.java.api.modules.questionnaire.pojo.PicFileDto;
import net.xfunction.java.api.modules.questionnaire.pojo.PicFileQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireDto;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireEntryDto;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireEntryQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireFormVo;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireMyListQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnaireMyQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnairePublicQuery;
import net.xfunction.java.api.modules.questionnaire.pojo.QuestionnairePublicVo;
import net.xfunction.java.api.modules.questionnaire.service.QuestionnaireService;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

	@Value("${xfunction.activity.images}")
	private String localActivityPath;
	
	@Resource 
	private XfuUploadImgMapper xfuUploadImgMapper;
	
	@Resource
	private XfuQuestionnaireFormMapper xfuQuestionnaireFormMapper;
	
	@Resource 
	private XfuQuestionnaireEntryMapper xfuQuestionnaireEntryMapper;
	
	@Override
	public PicFileDto savePic(PicFileQuery query) throws IllegalStateException, IOException {
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
		if(BaseUtils.isNull(query.getQuestionnaireId())) {
			img.setXfuUploadFunType(FileType.USERTEMP.getValue());  //1,活动的图片  2, sponsor 3,活动抽奖背景图,4 questionnarie ,99 temp 新增前 无相关Id
			img.setXfuUploadFunId(query.getUserId());
		}else {
			img.setXfuUploadFunType(FileType.QUESTIONAIREPIC.getValue());
			img.setXfuUploadFunId(query.getQuestionnaireId());
		}
		img.setXfuUploadUsed(0); // 0待定，1使用，2未使用
		img.setXfuUploadSrc(fileName);
		xfuUploadImgMapper.insertSelective(img);
		
		PicFileDto  dto = new PicFileDto();
		// dto.setFileId(img.getXfuUploadId());
		dto.setQuestionnairePic(fileName);
		return dto;
	}

	@Override
	public Result createUpdateQuestionnaireForm(QuestionnaireFormVo vo) {
		if(BaseUtils.isNull(vo.getQuestionnaireId())) { // 新增
			XfuQuestionnaireForm form = vo.convert();
			xfuQuestionnaireFormMapper.insertSelective(form);
			//处理图片
			Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
			WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
			c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.USERTEMP.getValue());//
			c.andEqualTo(XfuUploadImg::getXfuUploadFunId,form.getXfuUserId());
			c.andEqualTo(XfuUploadImg::getXfuUploadUsed, 0);//使用中的，待确认	
			List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
			for (XfuUploadImg xfuUploadImg : imageList) {
				if(xfuUploadImg.getXfuUploadSrc().equals(form.getXfuQuestionnairePic())) { // 找到了，如果找不到的由定时任务清理掉
					xfuUploadImg.setXfuUploadFunType(FileType.QUESTIONAIREPIC.getValue());
					xfuUploadImg.setXfuUploadFunId(form.getXfuQuestionnaireId());
					xfuUploadImg.setXfuUploadUsed(1);
					xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
					break; // 找到一个就行了，退出循环
				}
			}
			return Result.success(form.getXfuQuestionnaireId()); 			
		}else { // 更新
			XfuQuestionnaireForm form = vo.convert();
			xfuQuestionnaireFormMapper.updateByPrimaryKeySelective(form);
			//处理图片
			if(BaseUtils.isNotEmpty(form.getXfuQuestionnairePic())) {
				Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
				WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
				c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.QUESTIONAIREPIC.getValue());//
				c.andEqualTo(XfuUploadImg::getXfuUploadFunId,form.getXfuQuestionnaireId());
				List<Integer> para = new ArrayList<>();
				para.add(0);
				para.add(1);
				c.andIn(XfuUploadImg::getXfuUploadUsed, para);//使用中的，待确认	
				List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
				for (XfuUploadImg xfuUploadImg : imageList) {
					if(xfuUploadImg.getXfuUploadSrc().equals(form.getXfuQuestionnairePic())) {
						xfuUploadImg.setXfuUploadUsed(1);
						xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
					}else {
						xfuUploadImg.setXfuUploadUsed(2);
						xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
					}
				}
			}
			return Result.success(form.getXfuQuestionnaireId()); 
		}
		
	}
	
	@Override
	public Result getMyQuestionnaire(QuestionnaireMyQuery query) {
		XfuQuestionnaireForm  form = xfuQuestionnaireFormMapper.selectByPrimaryKey(query.getQuestionnaireId());
		if( BaseUtils.isNull(form)  ||  !form.getXfuUserId().equals(query.getUserId()) ) {
			return Result.failure(ResultCodeEnum.FAILD);
		}
		return Result.success(new QuestionnaireDto(form));
	}
	
	@Override
	public Result gePublicQuestionnaire(QuestionnairePublicQuery query) {
		XfuQuestionnaireForm  form = xfuQuestionnaireFormMapper.selectByPrimaryKey(query.getQuestionnaireId());
		if( BaseUtils.isNull(form) ) {
			return Result.failure(ResultCodeEnum.FAILD);
		}
		return Result.success(new QuestionnaireDto(form));
	}
	
	@Override
	public Result addQuestionnaireEntry(QuestionnairePublicVo vo) {
		
		XfuQuestionnaireEntry entry = vo.convert();
		xfuQuestionnaireEntryMapper.insertSelective(entry);
		
		XfuQuestionnaireForm form = xfuQuestionnaireFormMapper.selectByPrimaryKey(vo.getQuestionnaireId());
		form.setXfuQuestionnaireCount(form.getXfuQuestionnaireCount()+1);
		xfuQuestionnaireFormMapper.updateByPrimaryKeySelective(form);
		return Result.success();
	}
	
	@Override
	public void dlQuestionnaireEntries(QuestionnaireEntryQuery query,HttpServletResponse response) {
		XfuQuestionnaireForm form = xfuQuestionnaireFormMapper.selectByPrimaryKey(query.getQuestionnaireId());
		if( BaseUtils.isNull(form) || !form.getXfuUserId().equals(query.getUserId())) {
			return ;
		}
		Weekend<XfuQuestionnaireEntry> e = Weekend.of(XfuQuestionnaireEntry.class);
		WeekendCriteria<XfuQuestionnaireEntry, Object> c = e.weekendCriteria();
		c.andEqualTo(XfuQuestionnaireEntry::getXfuQuestionnaireId, query.getQuestionnaireId());
		List<XfuQuestionnaireEntry>  entryList = xfuQuestionnaireEntryMapper.selectByExample(e);
		
		// 组织表头
		List<List<String>> headList = new ArrayList<List<String>>();
		JSONArray questionArray = JSONObject.parseArray(form.getXfuQuestionnaireJson());
		for (int i = 0; i < questionArray.size(); i++) {  
			JSONObject jo = questionArray.getJSONObject(i); 
			List<String> head = new ArrayList<String>();
			// head.add(jo.getString("key"));
			head.add(jo.getString("label"));
			headList.add(head);
		}
		
		List<String> head = new ArrayList<String>();
		head.add("提交时间");	
		headList.add(head);
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );		
		List<List<String>> rowList = new ArrayList<List<String>>();
		for (XfuQuestionnaireEntry entry : entryList) {
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
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public Result getQuestionnaireEntries(QuestionnaireEntryQuery query) {
		XfuQuestionnaireForm form = xfuQuestionnaireFormMapper.selectByPrimaryKey(query.getQuestionnaireId());
		if( BaseUtils.isNull(form) || !form.getXfuUserId().equals(query.getUserId())) {
			return Result.failure(ResultCodeEnum.FAILD);
		}
		
		Weekend<XfuQuestionnaireEntry> e = Weekend.of(XfuQuestionnaireEntry.class);
		WeekendCriteria<XfuQuestionnaireEntry, Object> c = e.weekendCriteria();
		c.andEqualTo(XfuQuestionnaireEntry::getXfuQuestionnaireId, query.getQuestionnaireId());
		if(BaseUtils.isNotEmpty(query.getQueryStr())) c.andCondition("xfu_entry_content like '%" + query.getQueryStr()+"%' ");
		e.setOrderByClause("xfu_entry_id DESC");
		
		Page<XfuQuestionnaireEntry> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),true)
				.doSelectPage(() -> xfuQuestionnaireEntryMapper.selectByExample(e));
		
		List<QuestionnaireEntryDto> listDto = new ArrayList<>();
		for (XfuQuestionnaireEntry xfuQuestionnaireEntry : page.getResult()) {
			listDto.add(new QuestionnaireEntryDto(xfuQuestionnaireEntry));
		}
		
		PageResultSet<QuestionnaireEntryDto> resultSet = new PageResultSet<>();
		resultSet.setRows(listDto);		
		resultSet.setTotal(page.getTotal());
		
		if(query.getOffset() == 0) {
			resultSet.getDataDictMap().put("questionnaire", new QuestionnaireDto(form));
		}
		return Result.success(resultSet);
		
	}
	
	@Override
	public Result getMyQuestionnaires(QuestionnaireMyListQuery query) {
		
		Weekend<XfuQuestionnaireForm> e = Weekend.of(XfuQuestionnaireForm.class);
		WeekendCriteria<XfuQuestionnaireForm, Object> c = e.weekendCriteria();
		c.andEqualTo(XfuQuestionnaireForm::getXfuUserId, query.getUserId());
		c.andEqualTo(XfuQuestionnaireForm::getXfuQuestionnaireType,query.getQuestionnaireType());
		if(query.getShowExpired()) {
			c.andGreaterThan(XfuQuestionnaireForm::getXfuQuestionnaireExpired, new Date());
		}
		e.setOrderByClause("xfu_questionnaire_id DESC");
		
		Page<XfuQuestionnaireForm> page = PageHelper.offsetPage(query.getOffset(), query.getLimit(),true)
				.doSelectPage(() -> xfuQuestionnaireFormMapper.selectByExample(e));
		
		PageResultSet<QuestionnaireDto> resultSet = new PageResultSet<>();
		// 根据需要封装Dto
		List<QuestionnaireDto> listDto = new ArrayList<>();
		for (XfuQuestionnaireForm object : page.getResult()) {
			listDto.add(new QuestionnaireDto(object));
		}
		resultSet.setRows(listDto);
		resultSet.setTotal(page.getTotal());
		
		return Result.success(resultSet);
	}
}
