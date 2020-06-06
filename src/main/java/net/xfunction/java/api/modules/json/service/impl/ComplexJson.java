package net.xfunction.java.api.modules.json.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ComplexJson {

	public static void main(String[] args) throws Exception {
		BufferedReader reader = null;
		String laststr = "";
		File saveFile = new File("d:\\tags.json");
		FileInputStream fileInputStream = new FileInputStream(saveFile);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        reader = new BufferedReader(inputStreamReader);
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            laststr += tempString;
        }        
        reader.close();
        // System.out.println(laststr);
        
        JSONArray jsonArray = JSONObject.parseArray(laststr);
        for (int i = 0; i < jsonArray.size(); i++) {  
        	JSONObject level01Object = jsonArray.getJSONObject(i); 
        	String level01 = level01Object.getString("name");
        	String level02Str = level01Object.getString("subList");
        	JSONArray level02Array = JSONObject.parseArray(level02Str);
        	for ( int j = 0; j < level02Array.size(); j++) {
        		JSONObject level02Object = level02Array.getJSONObject(j);
        		String level02 = level02Object.getString("name");
        		String level03Str = level02Object.getString("subList");
            	JSONArray level03Array = JSONObject.parseArray(level03Str);
            	for(int k = 0; k< level03Array.size(); k ++) {
            		JSONObject level03Object = level03Array.getJSONObject(k);
            		String level03 = level03Object.getString("name");
            		System.out.println(level01 + ":" + level02 + ":" + level03);
            	}
        	}
        	
        }
	}

}
