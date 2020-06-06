package net.xfunction.java.api.modules.user.pojo;

import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;
import net.xfunction.java.api.modules.user.service.TokenService;

// DTO 输出
// VO 主要用于view 输入，尤其是add update
// Query 主要用于view 查询 输入

@Data
public class UserDto {

	// private String userName;
	private String userMobile;
	//private String userMail;
	private String accessToken;
	
	private String userAvatar;
	
	//用于填报方便使用
	private String name;
    private Integer sex;
    // @DateTimeFormat(pattern = "yyyy-MM-dd")  // 针对@requestbody是不起效的,且是单向的。所以使用以下注解
    @JsonFormat(pattern = "yyyy-MM-dd") //
    private Date birth;
    private String position;
    private String company;
    private String mobile;
    private String email;
    
   
    public UserDto(XfuUser user) {
    	this.accessToken = user.getXfuUserAuth(); //暂存的给正主
    	//this.setUserName(user.getXfuUserName());
    	//this.setUserMail(user.getXfuUserMail());
    	this.setUserMobile(user.getXfuUserMobile());
		this.setUserAvatar(user.getXfuUserAvatar());
    	this.setName(user.getXfuRealName());
    	this.setSex(user.getXfuRealSex());
    	this.setBirth(user.getXfuRealBirth());
    	this.setMobile(user.getXfuRealMobile());
    	this.setEmail(user.getXfuRealEmail());
    	this.setCompany(user.getXfuRealOrga());
    	this.setPosition(user.getXfuRealPosition());
    }
	
    public UserDto() {
    	
    }
    
}
