package net.xfunction.java.api.core.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import net.xfunction.java.api.core.annotation.PassToken;
import net.xfunction.java.api.core.annotation.UserLoginToken;
import net.xfunction.java.api.core.exception.BizException;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.user.service.UserBaseService;


public class AuthenticationInterceptor implements HandlerInterceptor {
	
	@Value("${xfunction.auth.secret}")
	private String authSecret;
	
	@Autowired
    UserBaseService userBaseService;
    
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                   	throw new BizException(ResultCodeEnum.UNAUTHORIZED);
                }
                
                
                // 验证 token,自身已经完成过期时间的判断
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(authSecret)).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                	throw new BizException(ResultCodeEnum.UNAUTHORIZED_TOKEN_ERROR);
                }                
                                
                //BizUserBaseinfo user = userService.findUserByUserID(userId);
                /*
                 * TODO 后面考虑对用户的状态进行不同的响应，比如被冻结，黑名单等
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                
                */
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, 
                                  HttpServletResponse httpServletResponse, 
                            Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, 
                                          HttpServletResponse httpServletResponse, 
                                          Object o, Exception e) throws Exception {
    }
}