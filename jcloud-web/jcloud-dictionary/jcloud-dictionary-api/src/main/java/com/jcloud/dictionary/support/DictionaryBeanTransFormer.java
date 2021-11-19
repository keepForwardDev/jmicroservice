package com.jcloud.dictionary.support;

import com.jcloud.common.service.Transformer;

import java.util.*;

/**
 * 字典映射转换基类
 * @author jiaxm
 * @date 2021/4/30
 */
public abstract class DictionaryBeanTransFormer<T, V> implements Transformer<T, V> {


    @Override
    public V convert(T t) {
        V v = convertToV(t);
        DictionaryMappingUtil.dictionaryMapping(Arrays.asList(v));
        return v;
    }

    @Override
    public List<V> convert(List<T> list) {
        List<V> resultList = new ArrayList<>();
        Map<String, Set<Long>> dictionaryMap = new HashMap<>();
        for (T t : list) {
            V v = convertToV(t);
            DictionaryMappingUtil.addDictionaryValue(v, dictionaryMap);
            resultList.add(v);
        }
        DictionaryMappingUtil.dictionaryMapping(resultList, dictionaryMap);
        return resultList;
    }

    /**
     * 字类实现单个转换
     * @param t
     * @return
     */
    protected abstract V convertToV(T t);
}
