package com.jcloud.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.util.List;
import java.util.Map;

public class BeanMapUtils {

    /**
     *@描述 将对象装换为map
     *@创建人 laiguowei
     *@参数
     *@返回值
     *@创建时间  2020/7/24

     *@修改人和其它信息

     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     *@描述 将map装换为javabean对象
     *@创建人 laiguowei
     *@参数
     *@返回值
     *@创建时间  2020/7/24

     *@修改人和其它信息

     */
    public static <T> T mapToBean(Map<String, Object> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     *@描述 将List<T>转换为List<Map<String, Object>>
     *@创建人 laiguowei
     *@参数
     *@返回值
     *@创建时间  2020/7/24

     *@修改人和其它信息

     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0,size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     *@描述 List<Map<String,Object>>转换为List<T>
     *@创建人 laiguowei
     *@参数
     *@返回值
     *@创建时间  2020/7/24

     *@修改人和其它信息

     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps,Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = Lists.newArrayList();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0,size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }
}
