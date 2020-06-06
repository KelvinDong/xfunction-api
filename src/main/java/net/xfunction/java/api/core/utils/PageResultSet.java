package net.xfunction.java.api.core.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.xfunction.java.api.core.pojo.DataDictDto;

/**
 * 通用分页返回结果集,此处加入可见数据权限的能力。
 *
 * @author Kelvin
 */
@Getter
@Setter
public class PageResultSet<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总数
     */
    private long total;
    /**
     * 返回的行数
     */
    private List<T> rows;
    
    
    private Map<String, Object> dataDictMap = new HashMap<String, Object>();
    
    
}
