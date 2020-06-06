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
		
		//读取图库目录
        File imgCatalog = new File(slideImages);
        File[] files = imgCatalog.listFiles();
 
        int randNum = new Random().nextInt(files.length);
        File targetFile = files[randNum];
        File tempImgFile = new File(slideTemplate);
        
        log.info(files.length + "  "  +randNum);
        log.info(targetFile.toString());
        log.info(tempImgFile.toString());
 
        //根据模板裁剪图片
        Slide result = PuzzleUtils.pictureTemplatesCut(tempImgFile,targetFile);        
        //sessionId 为key，value滑动距离X轴，缓存120秒
        redisService.set(session.getId(),result.getXWidth(),Duration.ofSeconds(300).getSeconds());	
        result.setXWidth(0);
		return Result.success(result);
	}
	/**
	   *     首页验证
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
			// redisService.del(session.getId()); 此处不能删除，待提交表单数据时还需要再次验证
			// 这里仅简单判断，二次验证时再完整处理
			return Result.success();
		}else {
			redisService.del(session.getId());
			return Result.failure(ResultCodeEnum.FAILD,"验证失败，请再试");
		}				
	}

}
