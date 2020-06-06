package net.xfunction.java.api.core.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import net.xfunction.java.api.core.mapper.xfunction.SysDictDataMapper;
import net.xfunction.java.api.core.model.xfunction.SysDictData;
import net.xfunction.java.api.core.pojo.DataDictDto;
import net.xfunction.java.api.core.service.DataDictionaryManagementService;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataDictionaryManagementServiceImpl implements DataDictionaryManagementService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;


    /*
     * @Cacheable不能配置  condition = "#result != null" ，因为这个注解在进入方法前去检测condition，而这时还没有result，会造成result为null的情况。
     * @CacheEvict,@Cacheput仅没有这个问题
     */
        
    @Override
    @Cacheable(value="DD",key="'dataDict-'.concat(#dictType)", condition ="#dictType!=null",unless ="#result.size()==0")
    public List<DataDictDto> findByDictType(String dictType) {

    	/*
    	 * TODO  Cacheable 中的 value 是静态值，如何与配置类一样从配置文件中获取？？
    	 */
        
    	/**
    	 * dict_sort  值最大排在前面
    	 */
        Weekend sysDictDataWeekend = Weekend.of(SysDictData.class) ;
        sysDictDataWeekend.orderBy(SysDictData.DICT_SORT).desc();
        WeekendCriteria<SysDictData, Object> criteria = sysDictDataWeekend.weekendCriteria();
        criteria.andEqualTo(SysDictData::getDictType,dictType).andEqualTo(SysDictData.STATUS,true);


        List<DataDictDto> dataDictDtoList = new ArrayList<>();

        sysDictDataMapper.selectByExample(sysDictDataWeekend).forEach(r->{
        	DataDictDto  dataDictDto = new DataDictDto(r);
        	dataDictDtoList.add(dataDictDto);
        });

        return dataDictDtoList;

    }

    @Override
    @Cacheable(value="DD",key="'dataDict-'.concat(#dictType).concat(#dictValue)", condition ="#dictType!=null",unless ="#result.size()==0")
    public DataDictDto findByDictType(String dictType, String dictValue) {

        Weekend sysDictDataWeekend = Weekend.of(SysDictData.class) ;
        sysDictDataWeekend.orderBy(SysDictData.DICT_SORT).desc();
        WeekendCriteria<SysDictData, Object> criteria = sysDictDataWeekend.weekendCriteria();
        criteria.andEqualTo(SysDictData::getDictType,dictType).andEqualTo(SysDictData::getDictValue, dictValue).andEqualTo(SysDictData.STATUS,true);
        DataDictDto dataDictDto = new DataDictDto(sysDictDataMapper.selectOneByExample(sysDictDataWeekend));
        return dataDictDto;

    }
}
