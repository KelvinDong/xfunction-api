package net.xfunction.java.api.modules.meeting.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.RedisService;

@Service
@Slf4j
public class AliMqttService {

	private static final String applyTokenUrl = "/token/apply";
	
	@Value("${xfunction.ali.mqtt.instanceid}")
	private String instanceId;
	@Value("${xfunction.ali.mqtt.expiretime}")
	private Long expireTime;
	@Value("${xfunction.ali.mqtt.accesskey}")
	private String accessKey;
	@Value("${xfunction.ali.mqtt.secretkey}")
	private String secretKey;
	@Value("${xfunction.ali.mqtt.apiurl}")
	private String apiUrl;
	
	@Value("${xfunction.ali.mqtt.groupid}")
	private String groupId;
	
	@Resource
    private RestTemplate restTemplate;
	
	@Resource
    RedisService redisService;
	
	public String applyToken(String topics, String action
			) throws InvalidKeyException, NoSuchAlgorithmException, IOException,
			KeyStoreException, UnrecoverableKeyException, KeyManagementException {
		
		Object cacheValueObject = redisService.get("mqtt::" + topics+action);
        if (BaseUtils.isNotNull(cacheValueObject)) {
        	return cacheValueObject.toString();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
         
        MultiValueMap<String, String> paramMap= new LinkedMultiValueMap<>();
        paramMap.add("resources", topics);
		paramMap.add("actions", action); //有“R”（读）未登录用户，“W”（写），“R,W”（读和写）登录用户
		paramMap.add("serviceName", "mq");
		paramMap.add("expireTime", String.valueOf(System.currentTimeMillis() + expireTime));
		paramMap.add("instanceId", instanceId);
		String signature = doHttpSignature(paramMap, secretKey);
		paramMap.add("proxyType", "MQTT");
		paramMap.add("accessKey", accessKey);
		paramMap.add("signature", signature);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap, headers);
        
        ResponseEntity<String> respString = restTemplate.postForEntity(apiUrl+applyTokenUrl, requestEntity, String.class);
        JSONObject jSONObj = JSON.parseObject(respString.getBody());
        String code = jSONObj.getString("tokenData");
		if (code != null) {
			redisService.set("mqtt::" + topics+action, code, Duration.ofMillis(expireTime).getSeconds()/6);
			return code;
		}
		return null;
	}
	
	
	/**
     * 计算签名，参数分别是参数对以及密钥
     *
     * @param requestParams 参数对，即参与计算签名的参数
     * @param secretKey 密钥
     * @return 签名字符串
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private String doHttpSignature(MultiValueMap<String, String> requestParams,
        String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        List<String> paramList = new ArrayList<String>();
        for (Entry<String, List<String>> entry : requestParams.entrySet()) {
        	List<String> valueList=entry.getValue();
        	Collections.sort(valueList);
        	StringBuffer result = new StringBuffer();
        	for(String str:valueList) {
        		result.append(str);
        		result.append(",");
        	}
        	if(result.length()>0) result.setLength(result.length()-1);
            paramList.add(entry.getKey() + "=" + result.toString());
        }
        // 要求必须进行排序
        Collections.sort(paramList);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < paramList.size(); i++) {
            if (i > 0) {
                sb.append('&');
            }
            sb.append(paramList.get(i));
        }
        log.debug(sb.toString());
        return macSignature(sb.toString(), secretKey);
    }

    /**
     * @param text 要签名的文本
     * @param secretKey 阿里云MQ secretKey
     * @return 加密后的字符串
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private String macSignature(String text,
        String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        Charset charset = Charset.forName("UTF-8");
        String algorithm = "HmacSHA1";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey.getBytes(charset), algorithm));
        byte[] bytes = mac.doFinal(text.getBytes(charset));
        return new String(Base64.encodeBase64(bytes), charset);
    }
}
