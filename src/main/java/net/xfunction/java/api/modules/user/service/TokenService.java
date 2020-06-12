package net.xfunction.java.api.modules.user.service;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;

import net.xfunction.java.api.core.exception.BizException;
import net.xfunction.java.api.core.utils.DateUtil;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;



/**
 * @author kelvin
 */
@Service
public class TokenService {

	@Value("${xfunction.auth.secret}")
	private String authSecret;
	
	@Value("${xfunction.auth.iss}")	
	private String authIssuer;
	
	@Value("${xfunction.auth.expired}")	
	private Integer authExpired;
	
	public String getToken(XfuUser user) {
        return JWT.create().withAudience(user.getXfuUserMobile())
                .withIssuer(authIssuer)
                .withClaim("userId", user.getXfuUserId())
                .withExpiresAt(DateUtil.addDay(new Date(), authExpired))
                .sign(Algorithm.HMAC256(authSecret));
    }
    
	
	public Long getUserIdFromRequest(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
		String token = httpServletRequest.getHeader("token");		
		return getUserIdFromToken(token);
	}

	/**
	 * 
	 * @param token
	 * @return null , Long
	 */
    public Long getUserIdFromToken(String token) {
        if (token == null) {
            return null;
        }
        // 获取 token 中的 user_id
        Long userId;
        try {
            userId = JWT.decode(token).getClaim("userId").asLong();
        } catch (JWTDecodeException j) {
            return null;
        }
        return userId;
    }
}
