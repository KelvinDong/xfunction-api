package net.xfunction.java.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.xfunction.java.api.core.interceptor.AuthenticationInterceptor;



@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Value("${xfunction.json.path}")
	private String localFilePath;
	
	@Value("${xfunction.activity.images}")
	private String localActivityPath;
	
	@Value("${xfunction.activity.audio}")
	private String localAudioPath;
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {        
    	registry.addResourceHandler("/JSON/**").addResourceLocations("file:"+localFilePath);
    	registry.addResourceHandler("/activity/images/**").addResourceLocations("file:"+localActivityPath);
    	registry.addResourceHandler("/activity/audio/**").addResourceLocations("file:"+localAudioPath)
    	.setCachePeriod(1);
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(authenticationInterceptor())
        .addPathPatterns("/**");   
	}

	@Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}
