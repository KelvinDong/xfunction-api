package net.xfunction.java.api.modules.user.pojo;

import lombok.Data;

@Data
public class WxDto {

	// private String jsapiTicket;

    private String nonceStr;

    private String timestamp;

    private String signature;

    private String appId;
    
}
