package net.xfunction.java.api.core.service;



import java.util.List;

import net.xfunction.java.api.core.pojo.DataDictDto;

public interface DataDictionaryManagementService {

	/**
	 * 根据字典类型获取内容
	 * @param dictType 字典类型
	 * @return
	 */
	List<DataDictDto> findByDictType(String dictType);

	/**
	 * 根据字典类型和值获取内容
	 * @param dictType 字典类型
	 * @param dictValue 字典值
	 * @return
	 */
	DataDictDto findByDictType(String dictType, String dictValue);


}
