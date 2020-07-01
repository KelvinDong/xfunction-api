package net.xfunction.java.api.modules.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.exception.BizException;
import net.xfunction.java.api.core.pojo.Slide;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.PuzzleUtils;
import net.xfunction.java.api.core.utils.RedisService;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;
import net.xfunction.java.api.modules.user.query.SlideQuery;

@RestController
@Slf4j
@RequestMapping("/slide")
public class SlideController {
	
	@Resource
	private RedisService redisService;
	
	@Value("${xfunction.slide.image}")
	private String slideImages;
	@Value("${xfunction.slide.template}")
	private String slideTemplate;
	
	@GetMapping("/get")
	public Result get( HttpServletRequest httpServletRequest) throws Exception {
		
		HttpSession session = httpServletRequest.getSession();
		
		log.info("get--- get's session's id" + session.getId());
		
        File imgCatalog = new File(slideImages);
        File[] files = imgCatalog.listFiles();
 
        int randNum = new Random().nextInt(files.length);
        File targetFile = files[randNum];
        File tempImgFile = new File(slideTemplate);
        
        log.info(files.length + "  "  +randNum);
        log.info(targetFile.toString());
        log.info(tempImgFile.toString());
 
        Slide slide = PuzzleUtils.pictureTemplatesCut(tempImgFile,targetFile);        

        redisService.set(session.getId(),slide.getXWidth(),Duration.ofSeconds(300).getSeconds());	
        slide.setXWidth(0);
		return Result.success(slide);
	}
	
	/**
	 * First validation, for initial feedback on the validity of the jigsaw puzzle, so there is no deletion of the cached data.
	   When the actual business data is submitted, make another judgment.
	 * @param slideQuery
	 * @param httpServletRequest
	 * @return
	 */
	@PostMapping("/vertify")
	public Result vertify(@RequestBody SlideQuery slideQuery ,HttpServletRequest httpServletRequest) {		
		
		HttpSession session = httpServletRequest.getSession();
		log.info("vertify--- get's session's id" + session.getId());
		log.info(slideQuery.toString());
		
		Integer xWidth = (Integer) redisService.get(session.getId());
		if(BaseUtils.isNull(xWidth)) {
			redisService.del(session.getId());
			return Result.failure(ResultCodeEnum.PARAMS_MISS);
		}else if(Math.abs(xWidth-slideQuery.getMove())<=2) {
			// Do not delete cached data.
			return Result.success();
		}else {
			redisService.del(session.getId());
			return Result.failure(ResultCodeEnum.FAILD,"Verification failed. Please try again");
		}				
	}

}
