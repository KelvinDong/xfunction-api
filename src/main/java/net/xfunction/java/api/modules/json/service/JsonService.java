package net.xfunction.java.api.modules.json.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.json.pojo.ParseVo;

public interface JsonService {

	

	Result ParseJson(ParseVo parse) throws IOException;

}
