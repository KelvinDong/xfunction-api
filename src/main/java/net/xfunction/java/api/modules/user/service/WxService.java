package net.xfunction.java.api.modules.user.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.user.pojo.WxDto;

@Slf4j
@Service
public class WxService {

	@Value("${xfunction.wx.appid}")
	private String appId;

	@Value("${xfunction.wx.secret}")
	private String secret;

	public static String accessToken = null;
	public static String jsApiTicket = null;

	public static Long getTokenTime = 0L;
	public static Long tokenExpireTime = 0L;

	public static Long getTicketTime = 0L;
	public static Long ticketExpireTime = 0L;

	
	public WxDto getWxJsParam(String url) throws Exception {
		
		// log.info(url);
		Long now = System.currentTimeMillis();
		if(BaseUtils.isNull(accessToken)
				|| (now - getTokenTime > tokenExpireTime * 1000))
			getAccessTokenForJS();
		if(BaseUtils.isNull(jsApiTicket)
				|| (now - getTicketTime > ticketExpireTime * 1000))
			getJsApiTicket();
		
		// log.info(accessToken , getTokenTime ,tokenExpireTime,"---" ,jsApiTicket ,getTicketTime,ticketExpireTime);
		
		return sign(url);
			
	}
	
	
	private WxDto sign(String url) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		WxDto wxDto = new WxDto();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsApiTicket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(string1.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());

        // wxDto.setJsapiTicket(jsApiTicket);
        wxDto.setNonceStr(nonce_str);
        wxDto.setTimestamp(timestamp);
        wxDto.setAppId(appId);
        wxDto.setSignature(signature);
        // log.info(wxDto.toString());
        return wxDto;
	}
	
	private void getAccessTokenForJS() throws Exception {
		String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		String requestUrl = accessTokenUrl.replace("APPID", appId).replace("APPSECRET", secret);
		JSONObject result = doGet(requestUrl);
		if(BaseUtils.isNotNull(result)) {
			accessToken = result.getString("access_token");
			tokenExpireTime = result.getLongValue("expires_in");
			getTokenTime = System.currentTimeMillis();
		}
	}

	private void getJsApiTicket() throws Exception {
		String apiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		String accessTokenTemp = accessToken != null ? accessToken : "";
		String requestUrl = apiTicketUrl.replace("ACCESS_TOKEN", accessTokenTemp);
		JSONObject result = doGet(requestUrl);
		if(BaseUtils.isNotNull(result)) {
			jsApiTicket = result.getString("ticket");
			ticketExpireTime = result.getLongValue("expires_in");
			getTicketTime = System.currentTimeMillis();
		}
	}

	private String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	private JSONObject doGet(String requestUrl) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response;
		String responseContent;
		com.alibaba.fastjson.JSONObject result;
		// 创建Get请求，
		HttpGet httpGet = new HttpGet(requestUrl);
		// 执行Get请求，
		response = httpClient.execute(httpGet);
		// 得到响应体
		HttpEntity entity = response.getEntity();
		// 获取响应内容
		responseContent = EntityUtils.toString(entity, "UTF-8");
		// 转换为map
		result = JSON.parseObject(responseContent);
		return result;
	}

}
