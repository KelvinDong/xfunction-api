package net.xfunction.java.api.modules.json.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.json.controller.JsonController;
import net.xfunction.java.api.modules.json.pojo.ParseVo;
import net.xfunction.java.api.modules.json.service.JsonService;


@Service
@Slf4j
public class JsonServiceImpl implements JsonService {
	
	//本地保存的临时文件夹，用完需删除
	@Value("${xfunction.json.path}")
	private String localFilePath;
		
	@Override
	public Result ParseJson(ParseVo parse) throws IOException  {
		
		
		MultipartFile upFile = parse.getJsonFile();		
		String suffixName = upFile.getOriginalFilename().substring(upFile.getOriginalFilename().lastIndexOf("."));        
        File saveFile = new File(localFilePath + UUID.randomUUID()+suffixName);
        upFile.transferTo(saveFile);
        
		BufferedReader reader = null;
		String laststr = "";
		
		// 获取json文件，转换成str
		FileInputStream fileInputStream = new FileInputStream(saveFile);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        reader = new BufferedReader(inputStreamReader);
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            laststr += tempString;
        }        
        reader.close();
        
        // 转换为 jsonarray
        Set<String> keySet = new HashSet<String>();
        JSONArray jsonArray = JSONObject.parseArray(laststr);
        
        //获取所有可能存在的key
        for (int i = 0; i < jsonArray.size(); i++) {  
        	JSONObject jo = jsonArray.getJSONObject(i); 
        	for (Map.Entry<String, Object> entry : jo.entrySet()) {
                // System.out.println(entry.getKey() + ":" + entry.getValue());
        		keySet.add(entry.getKey());
            }
        }
        
        //创建结果临时文件
        File  csvFile = new File(saveFile.getAbsolutePath()+".csv");       
        BufferedWriter  csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8), 1024);        
        StringBuffer sb = new StringBuffer();
        
        // 解决excel打开乱码的问题
        csvFileOutputStream.write('\ufeff');

        // 先写入所有的key 一行
        for(String temp : keySet) {
        	sb.append(temp).append(",");
        }
        csvFileOutputStream.write(sb.toString());
        csvFileOutputStream.newLine();
        
        // 循环写入每一行，同时对CSV中最重要的 逗号 换行进行处理。
        for (int i = 0; i < jsonArray.size(); i++) {  
        	JSONObject jo = jsonArray.getJSONObject(i); 
        	sb = new StringBuffer();
	        for(String temp : keySet) {	        	
	        	if(BaseUtils.isNull(jo.get(temp))) {
	        		sb.append(",");
	        	}else {
	        		sb.append(jo.get(temp).toString().replaceAll(",", "，").replaceAll("\r", "").replaceAll("\n", "\\\\n")).append(",");
	        	}	        	
	        }
	        csvFileOutputStream.write(sb.toString());
	        csvFileOutputStream.newLine();
        }
        
		csvFileOutputStream.close();
        
		return Result.success(csvFile.getName());
	}
	
	
	
	@Data
	static class KeyValue {
		private int id;
		private String value;
	}

}
