package net.xfunction.java.api.core.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface XfunMapper<T> extends Mapper<T>,MySqlMapper<T> {

}
