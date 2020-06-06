package net.xfunction.java.api.modules.user.service;

import java.io.UnsupportedEncodingException;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUserReset;


@Service
public class AuthHelper {

	@Value("${spring.mail.from}")
	private String mailFrom;
	
	/*
	@Value("${xfunction.user.bind}")
	private String bindUrl;
	
	@Value("${xfunction.user.reset}")
	private String resetUrl;
	
	@Value("${xfunction.web.host}")
	private String webHost;
	
	
	@Autowired
    JavaMailSender jms;
    */
	
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public void encryptAuth(XfuUser user) {
    	
    	user.setXfuUserAuthSalt(randomNumberGenerator.nextBytes().toHex());

        String newAuth = new SimpleHash(
                algorithmName,
                user.getXfuUserAuth(),
                ByteSource.Util.bytes(user.getXfuUserAuthSalt()),
                hashIterations).toHex();

        user.setXfuUserAuth(newAuth);
    }

    public String encryptAuthBysalt(String auth, String salt) {

        String newAuth = new SimpleHash(
                algorithmName,
                auth,
                ByteSource.Util.bytes(salt),
                hashIterations).toHex();

        return newAuth;
    }
    
    /*
    public void sendResetMail(XfuUserReset userReset) throws UnsupportedEncodingException {
    	String fromByte = new String(("Xfunction" + " <" + mailFrom + ">").getBytes("UTF-8"));  //呢称，客户端不直接显示邮件地址  	
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromByte);
        message.setTo(userReset.getXfuUserMail());
        if(BaseUtils.isNotNull(userReset.getXfuUserId())) {
	        message.setSubject("邮箱绑定");
	        message.setText("尊敬的用户:\r\n"
	        		+ "您的选择，就是对我们最大的信任\r\n"
	        		+ "请点击完成邮箱绑定 "+this.webHost + this.bindUrl + "?token="+ userReset.getXfuResetToken());
        }else {
        	message.setSubject("密码重置");
            message.setText("请点击重置密码 "+this.webHost + this.resetUrl + "?token="+ userReset.getXfuResetToken());
        }
        jms.send(message);
    }
    */
    
}
