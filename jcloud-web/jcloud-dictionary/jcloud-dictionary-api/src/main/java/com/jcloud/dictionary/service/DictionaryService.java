package com.jcloud.dictionary.service;

import com.jcloud.dictionary.entity.DictionaryBase;

/**
 * @author jiaxm
 * @date 2021/4/12
 */
public interface DictionaryService<T extends DictionaryBase> extends DictionaryCommon<T> {

    /**
     * 字典存在redis的key
     * @return
     */
    public String getDictionaryKey();

    /**
     * 缓存初始化
     */
    public void dataToRedis();

    /**
     * 专门用于支撑 根据名称拿对象的
    * @Author: laiguowei
    * @Description:
    * @DateTime: 2021/8/3/003 10:49
    * @Params: [key, name]
    * @Return T
    */
    public T getNameForObject(String key, String name);


}
