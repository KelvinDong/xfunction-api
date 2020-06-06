package net.xfunction.java.api.modules.user.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;


import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.ibatis.javassist.runtime.DotClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.xfunction.java.api.core.enums.FileType;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.DateUtil;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.core.utils.SmsService;
import net.xfunction.java.api.modules.activity.mapper.xfunction.XfuUploadImgMapper;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuUploadImg;
import net.xfunction.java.api.modules.activity.pojo.sponsor.SponsorVo;
import net.xfunction.java.api.modules.user.mapper.xfunction.XfuUserMapper;
import net.xfunction.java.api.modules.user.mapper.xfunction.XfuUserResetMapper;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUserReset;
import net.xfunction.java.api.modules.user.pojo.UserDto;

import net.xfunction.java.api.modules.user.service.AuthHelper;
import net.xfunction.java.api.modules.user.service.TokenService;
import net.xfunction.java.api.modules.user.service.UserBaseService;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class UserBaseServiceImpl implements UserBaseService {

	@Value("${xfunction.activity.images}")
	private String localActivityPath;
	
	@Resource
	private AuthHelper authHelper;
	
	@Resource 
	private XfuUploadImgMapper xfuUploadImgMapper;
	
	@Resource
	private XfuUserMapper xfuUserMapper;
	
	@Resource
	private TokenService tokenService;
	
	@Resource
	private XfuUserResetMapper xfuUserResetMapper;
	
	@Resource
	private SmsService smsService;
	
	/*
	@Value("${xfunction.mail.expired}")
	private Integer mailExpired;
	
	@Value("${xfunction.mail.more}")
	private Integer mailMore;
	*/
	
	/*1、合法性较验由controller层完成 ，Service不再处理
	 * 2、防止客户端参数杂多， Service 需要根据需求重新生成 数据库对像，  不能使用直接传参过来的参数，尤其是insert和update*/	 
	
	/*
	@Override
	public Result createUser(XfuUser user) {				
		Weekend<XfuUser> example = Weekend.of(XfuUser.class);
		WeekendCriteria<XfuUser, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuUser::getXfuUserName, user.getXfuUserName());		
		XfuUser exist = xfuUserMapper.selectOneByExample(example);
		if(BaseUtils.isNull(exist)) {
			authHelper.encryptAuth(user);
			xfuUserMapper.insertSelective(user);	
			return Result.success();//返回Token
		}else {
			if(exist.getXfuUserBlocked()) {//帐号被冻结
				return Result.failure(ResultCodeEnum.USER_BLOCKED);
			}
			return   Result.failure(ResultCodeEnum.USER_EXISTS);   //帐户已经存在
		}		
	}
	
	@Override
	public Result findUserByAuth(XfuUser user) {					
		Weekend<XfuUser> example = Weekend.of(XfuUser.class);
		WeekendCriteria<XfuUser, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuUser::getXfuUserName,user.getXfuUserName());
		XfuUser resultUser = xfuUserMapper.selectOneByExample(example);
		if(BaseUtils.isNull(resultUser)) {
			return Result.failure(ResultCodeEnum.USER_NOT_EXISTS);
		}else {			
			if(!authHelper.encryptAuthBysalt(user.getXfuUserAuth(), resultUser.getXfuUserAuthSalt()).equals(resultUser.getXfuUserAuth())) {
				return Result.failure(ResultCodeEnum.USER_WRONG_AUTH);
			}			
			if(resultUser.getXfuUserBlocked()) {//帐号被冻结
				return Result.failure(ResultCodeEnum.USER_BLOCKED);
			}
			resultUser.setXfuUserAuth(tokenService.getToken(resultUser)); //借用存储
			UserDto dto = new UserDto(resultUser);
			return Result.success(dto);
		}
	}
	
	
	
	
	@Override
	public XfuUser getXfuUser(XfuUser user) {					
		
		XfuUser resultUser = xfuUserMapper.selectByPrimaryKey(user.getXfuUserId());
			
			return (resultUser);
		
	}
	
	@Override
	public void updateUserByAuth(XfuUser user) {			
		XfuUser resultUser = xfuUserMapper.selectByPrimaryKey(user.getXfuUserId());
		resultUser.setXfuUserAuth(authHelper.encryptAuthBysalt(user.getXfuUserAuth(),resultUser.getXfuUserAuthSalt()));
		resultUser.setXfuUserUpdateDate(new Date());
		xfuUserMapper.updateByPrimaryKeySelective(resultUser);
	}
	*/
	
	
	
	
	@Override
	public Result getUser(XfuUser user) {					
		
		XfuUser resultUser;
		if(BaseUtils.isNotEmpty(user.getXfuUserMobile()))  // 手机号注册及登录 
		{
			resultUser = xfuUserMapper.selectOne(user);
			if(BaseUtils.isNull(resultUser)) xfuUserMapper.insert(user);			
		}else {
			resultUser = xfuUserMapper.selectByPrimaryKey(user.getXfuUserId());
		}
		resultUser.setXfuUserAuth(tokenService.getToken(resultUser));
		UserDto dto = new UserDto(resultUser);
		return Result.success(dto);
		
	}
	@Override
	public String saveUserPic(XfuUser user, MultipartFile file) throws IllegalStateException, IOException {
		// 文件存储
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
		img.setXfuUploadFunType(FileType.USERAVATAR.getValue());  
		img.setXfuUploadFunId(user.getXfuUserId());
		img.setXfuUploadUsed(0); // 0待定，1使用，2未使用
		img.setXfuUploadSrc(fileName);
		xfuUploadImgMapper.insertSelective(img);
		return fileName;
	}
	
	// 前提  确保 user 安全
	@Override
	public Result updateUser(XfuUser user) {
		xfuUserMapper.updateByPrimaryKeySelective(user);
		if(BaseUtils.isNotEmpty(user.getXfuUserAvatar())) {
			//标注已经失效的内容图片
			Weekend<XfuUploadImg> e = Weekend.of(XfuUploadImg.class);
			WeekendCriteria<XfuUploadImg, Object> c = e.weekendCriteria();
			c.andEqualTo(XfuUploadImg::getXfuUploadFunType,FileType.USERAVATAR.getValue());//主办方的Logo
			c.andEqualTo(XfuUploadImg::getXfuUploadFunId,user.getXfuUserId());
			List<Integer> para = new ArrayList<>();
			para.add(0);
			para.add(1);
			c.andIn(XfuUploadImg::getXfuUploadUsed, para);//使用中的，待确认	
			List<XfuUploadImg> imageList = xfuUploadImgMapper.selectByExample(e);
			for (XfuUploadImg xfuUploadImg : imageList) {
				if(user.getXfuUserAvatar().contains(xfuUploadImg.getXfuUploadSrc())) { 
					xfuUploadImg.setXfuUploadUsed(1);
					xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
				}else {			
					xfuUploadImg.setXfuUploadUsed(2);
					xfuUploadImgMapper.updateByPrimaryKeySelective(xfuUploadImg);
				}
			}
		}
		return this.getUser(user);
	}
	
	/*
	@Override
	public XfuUser isMobleExist(String userMoble) {
		Weekend<XfuUser> example = Weekend.of(XfuUser.class);
		WeekendCriteria<XfuUser, Object> criteria = example.weekendCriteria();
		criteria.andEqualTo(XfuUser::getXfuUserMobile,userMoble);
		XfuUser resultUser = xfuUserMapper.selectOneByExample(example);
		return resultUser;
	}
	
	
	//拟发送邮箱绑定邮件
	@Override
	public Result sendBindMail(XfuUser user) {
		//id,mail
		XfuUser mailUser = new XfuUser();
		mailUser.setXfuUserMail(user.getXfuUserMail());
		mailUser = xfuUserMapper.selectOne(mailUser);		
		if(!BaseUtils.isNull(mailUser)) {
			return Result.failure(ResultCodeEnum.MAIL_BINDED);
		}		
		return sendTokenMail(user);
	}
	
	//通过邮件内容绑定邮箱地址
	@Override
	public Result bindResult(UserQuery userQuery) {
		Weekend<XfuUserReset> resetExample = Weekend.of(XfuUserReset.class);
		WeekendCriteria<XfuUserReset,Object> resetCriteria = resetExample.weekendCriteria();
		resetCriteria.andEqualTo(XfuUserReset::getXfuResetToken, userQuery.getToken());		
		resetCriteria.andIsNotNull(XfuUserReset::getXfuUserId);		
		resetCriteria.andGreaterThan(XfuUserReset::getXfuResetExpired, new Date());
		resetCriteria.andEqualTo(XfuUserReset::getXfuResetHas,false);
		XfuUserReset resultXfuUserReset = xfuUserResetMapper.selectOneByExample(resetExample);
		if(BaseUtils.isNull(resultXfuUserReset)) {
			return Result.failure(ResultCodeEnum.FAILD);
		}else {
			resultXfuUserReset.setXfuResetHas(true);
			resultXfuUserReset.setXfuUpdateDate(new Date());
			xfuUserResetMapper.updateByPrimaryKeySelective(resultXfuUserReset);
			XfuUser user = new XfuUser();
			user.setXfuUserId(resultXfuUserReset.getXfuUserId());
			user.setXfuUserMail(resultXfuUserReset.getXfuUserMail());
			user.setXfuUserUpdateDate(new Date());
			xfuUserMapper.updateByPrimaryKeySelective(user);
			return Result.success();
		}
		
	}



	//拟发送密码重置邮件
		@Override
		public Result sendPasswordMail(XfuUser user){
			// mail
			Weekend<XfuUser> example = Weekend.of(XfuUser.class);
			WeekendCriteria<XfuUser, Object> criteria = example.weekendCriteria();
			criteria.andEqualTo(XfuUser::getXfuUserMail, user.getXfuUserMail());	
			XfuUser resultUser = xfuUserMapper.selectOneByExample(example);
			if(BaseUtils.isNull(resultUser)) {
				return Result.failure(ResultCodeEnum.USER_NOT_EXISTS);
			}else {
				if(resultUser.getXfuUserBlocked()) return Result.failure(ResultCodeEnum.USER_BLOCKED);			
				return sendTokenMail(user);
			}
			
		}	
	
	//通过邮件内容,进行密码重置
	@Override
	public Result resetFromMail(UserQuery userQuery){
		Weekend<XfuUserReset> resetExample = Weekend.of(XfuUserReset.class);
		WeekendCriteria<XfuUserReset,Object> resetCriteria = resetExample.weekendCriteria();
		resetCriteria.andEqualTo(XfuUserReset::getXfuResetToken, userQuery.getToken());		
		resetCriteria.andIsNull(XfuUserReset::getXfuUserId);  						
		resetCriteria.andGreaterThan(XfuUserReset::getXfuResetExpired, new Date());
		resetCriteria.andEqualTo(XfuUserReset::getXfuResetHas,false);
		XfuUserReset resultXfuUserReset = xfuUserResetMapper.selectOneByExample(resetExample);
		if(BaseUtils.isNull(resultXfuUserReset)) {
			resultXfuUserReset.setXfuResetHas(true);
			resultXfuUserReset.setXfuUpdateDate(new Date());
			xfuUserResetMapper.updateByPrimaryKeySelective(resultXfuUserReset);			
			XfuUser xfuUser = new XfuUser();
			xfuUser.setXfuUserMail(resultXfuUserReset.getXfuUserMail());
			xfuUser = xfuUserMapper.selectOne(xfuUser);
			xfuUser.setXfuUserAuth(authHelper.encryptAuthBysalt(userQuery.getUserAuth(),xfuUser.getXfuUserAuthSalt()));
			xfuUser.setXfuUserUpdateDate(new Date());
			xfuUserMapper.updateByPrimaryKeySelective(xfuUser);
			return Result.success();
		}else {
			return Result.failure(ResultCodeEnum.FAILD);
		}
	}
	
	
	//发送邮件
	private Result sendTokenMail(XfuUser user) {
		// mail + id 为绑定邮箱
		// mail 邮箱重置密码
		
		//是否之前还存在有效的记录
		Weekend<XfuUserReset> resetExample = Weekend.of(XfuUserReset.class);
		WeekendCriteria<XfuUserReset,Object> resetCriteria = resetExample.weekendCriteria();
		resetCriteria.andEqualTo(XfuUserReset::getXfuUserMail, user.getXfuUserMail());
		if(BaseUtils.isNull(user.getXfuUserId())) {
			resetCriteria.andIsNull(XfuUserReset::getXfuUserId);
		}else {
			resetCriteria.andEqualTo(XfuUserReset::getXfuUserId,user.getXfuUserId());
		}
		resetCriteria.andGreaterThan(XfuUserReset::getXfuResetExpired, new Date());
		resetCriteria.andEqualTo(XfuUserReset::getXfuResetHas,false);
		XfuUserReset resultXfuUserReset = xfuUserResetMapper.selectOneByExample(resetExample);
		
		if(BaseUtils.isNotNull(resultXfuUserReset)) {
			if(resultXfuUserReset.getXfuResetTimes()>= mailMore ) return Result.failure(ResultCodeEnum.SEND_MAIL_MORE);
			//准备重新发送	
			resultXfuUserReset.setXfuResetTimes(resultXfuUserReset.getXfuResetTimes()+1);
			xfuUserResetMapper.updateByPrimaryKeySelective(resultXfuUserReset);
		}else {
			//生成新的记录
			resultXfuUserReset = new XfuUserReset();			
			resultXfuUserReset.setXfuUserMail(user.getXfuUserMail());
			resultXfuUserReset.setXfuUserId(user.getXfuUserId());
			String tempString = (UUID.randomUUID().toString()+user.getXfuUserMail());
			resultXfuUserReset.setXfuResetToken(Md5Crypt.md5Crypt(tempString.getBytes()));
			resultXfuUserReset.setXfuResetExpired(DateUtil.addDay(new Date(), mailExpired));
			xfuUserResetMapper.insertSelective(resultXfuUserReset);					
		}
		//发邮件
		try {
			authHelper.sendResetMail(resultXfuUserReset);
		} catch (UnsupportedEncodingException e) {
			return Result.failure(ResultCodeEnum.SEND_MAIL_FAIL);
		}	
		return Result.success();
	}
	*/
}


