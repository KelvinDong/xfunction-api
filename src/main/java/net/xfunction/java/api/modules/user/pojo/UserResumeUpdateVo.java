package net.xfunction.java.api.modules.user.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;

/**
 * 用于更新 用户方便填写信息的信息
 * @author bandg
 *
 */
@Data
public class UserResumeUpdateVo {
	
	private String name;
	private String avatar;
    private Integer sex;
    
    // @DateTimeFormat(pattern = "yyyy-MM-dd")  // 针对@requestbody是不起效的,且是单向的。所以使用以下注解
    @JsonFormat(pattern = "yyyy-MM-dd") //双向
    private Date birth;
    
    private String position;
    private String company;
    private String mobile;
    private String email;
    
    private MultipartFile file;
    
    
    public XfuUser convertToXfuUser() {
    	XfuUser user= new XfuUser();
    	user.setXfuRealBirth(this.birth);
    	user.setXfuRealEmail(this.email);
    	user.setXfuRealMobile(this.mobile);
    	user.setXfuRealName(this.name);
    	user.setXfuUserAvatar(this.avatar);
    	user.setXfuRealOrga(this.company);
    	user.setXfuRealPosition(this.position);
    	user.setXfuRealSex(this.sex);
    	return user;
    }
    
}
