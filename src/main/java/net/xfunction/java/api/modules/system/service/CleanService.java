package net.xfunction.java.api.modules.system.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.DateUtil;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuUploadImgMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuUploadImg;
import net.xfunction.java.api.modules.activity.service.impl.MainServiceImpl;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 清理 媒体图片
 * 
 * @author bandg
 *
 */
@Component
@EnableScheduling
@Slf4j
public class CleanService {

	@Value("${xfunction.activity.images}")
	private String localActivityPath;

	@Value("${xfunction.ali.mqtt.accesskey}")
	private String accessKey;
	@Value("${xfunction.ali.mqtt.secretkey}")
	private String secretKey;
	@Value("${xfunction.ali.meeting.appId}")
	private String appId;
	
	@Resource
	private XfuUploadImgMapper xfuUploadImgMapper;

	/*
	 * 
	 * 微信分享 简易抽奖 签到墙 调查问卷 信息收集
	 */
	@Scheduled(cron = "0 0 */1 * * *") // 每一个小时清理一次文件并设置删除位， 数据库是在新增活动时由触发器来delete 处于删除位的记录
	// @Scheduled(cron = "0 */1 * * * *") // 每一个小时清理一次文件并设置删除位，
	// 数据库是在新增活动时由触发器来delete 处于删除位的记录
	public void run() {
		Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
		WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
		c.andEqualTo(XfuUploadImg::getXfuUploadUsed, 2); // 未使用的
		c.andEqualTo(XfuUploadImg::getXfuDelStatus, false); // 仍未删除的
		List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
		for (XfuUploadImg xfuUploadImg : imageList) {
			log.debug(localActivityPath + xfuUploadImg.getXfuUploadSrc());
			File dest = new File(localActivityPath + xfuUploadImg.getXfuUploadSrc());
			dest.delete();
			xfuUploadImg.setXfuDelStatus(true);
			xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
		}

		Weekend<XfuUploadImg> e1 = Weekend.of(XfuUploadImg.class);
		WeekendCriteria<XfuUploadImg, Object> c1 = e1.weekendCriteria();
		c1.andEqualTo(XfuUploadImg::getXfuUploadUsed, 0); // 待定的
		c1.andEqualTo(XfuUploadImg::getXfuDelStatus, false); // 仍未删除的
		c1.andLessThan(XfuUploadImg::getXfuCreate, DateUtil.addDay(new Date(), -1)); // 超过24小时的
		imageList = xfuUploadImgMapper.selectByExample(e1);
		for (XfuUploadImg xfuUploadImg : imageList) {
			log.debug(localActivityPath + xfuUploadImg.getXfuUploadSrc());
			File dest = new File(localActivityPath + xfuUploadImg.getXfuUploadSrc());
			dest.delete();
			xfuUploadImg.setXfuDelStatus(true);
			xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
		}

	}

	@Scheduled(cron = "0 */10 * * * *")
	public void cleanRoom() {

		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKey, secretKey);
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("rtc.aliyuncs.com");
		request.setVersion("2018-01-11");
		request.setAction("DescribeChannelUsers");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("ChannelId", "111111");
		request.putQueryParameter("AppId", appId);
		try {
			CommonResponse response = client.getCommonResponse(request);
			JSONObject jSONObj = JSON.parseObject(response.getData());
			JSONArray list = jSONObj.getJSONArray("UserList");
			StringBuilder sb = new StringBuilder();  
			for (int i = 0; i < list.size(); i++) {  
		        sb.append(list.get(i)).append(",");  
		    } 			
			log.info(jSONObj.toJSONString());
			if(list.size()>0) {
				request.setAction("RemoveTerminals");
				request.putQueryParameter("TerminalIds."+list.size(), sb.toString().substring(0, sb.toString().length() - 1));	
				client.getCommonResponse(request);
			}
								
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}



		/*
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("rtc.aliyuncs.com");
		request.setVersion("2018-01-11");
		request.setAction("RemoveTerminals");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		*/

	}
}
