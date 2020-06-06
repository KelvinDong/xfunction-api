package net.xfunction.java.api.modules.json.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.json.pojo.ParseVo;
import net.xfunction.java.api.modules.json.service.JsonService;
import net.xfunction.java.api.modules.shortlink.model.xfunction.BizShortLink;


@RestController
@Slf4j
@RequestMapping("/json")
public class JsonController {

	@Resource
	private JsonService jsonService;
	
	@PostMapping("/parse")
	Result parse(ParseVo parse, HttpServletRequest httpServletRequest) throws IOException{   //@RequestBody json提效，无此注解是键值对		
		return jsonService.ParseJson(parse);
	}
}
