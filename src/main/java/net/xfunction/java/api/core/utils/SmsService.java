package net.xfunction.java.api.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import net.xfunction.java.api.core.pojo.Sms;

@Service
public class SmsService {

	
	@Value("${xfunction.sms.send}")
	private boolean smsSend;
	
	@Value("${xfunction.sms.secret}")
	private String smsSecret;
	
	@Value("${xfunction.sms.key}")
	private String smsKey;
	
	@Value("${xfunction.sms.signname}")
	private String smsSignname;
	
	/**
	 * TODO　以后有时间常量  改到配置中去实现
	 * @param sms
	 */
	public void sendSms(Sms sms) {
		if(!smsSend) return;
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsKey, smsSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", sms.getMobile());
        request.putQueryParameter("SignName", smsSignname);
        request.putQueryParameter("TemplateCode", "SMS_175537576");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+ sms.getCode() +"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject json = JSONObject.parseObject(response.getData());
            //json.get("Code");
            System.out.println(json.get("Code"));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
