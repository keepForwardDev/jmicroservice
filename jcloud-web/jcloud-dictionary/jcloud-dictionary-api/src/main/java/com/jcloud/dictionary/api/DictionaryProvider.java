package com.jcloud.dictionary.api;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.jcloud.dictionary.service.DictionaryService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 字典服务提供者
 * DictionaryProvider.service().xxx
 * @author jiaxm
 * @date 2021/4/13
 */
@Component
public class DictionaryProvider implements ApplicationContextAware {

    @Resource(name = "dictionaryRedisTemplate")
    protected RedisTemplate<String, Object> dictionaryRedisTemplate;

    /**
     * 字典名称 map
     */
    private static Map<String, DictionaryService> dictionaryServiceNameMap;

    /**
     * 字典类名map
     */
    private static Map<String, DictionaryService> dictionaryServiceClassNameMap = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        dictionaryServiceNameMap = applicationContext.getBeansOfType(DictionaryService.class);
        dictionaryServiceNameMap.forEach((k, v) -> {
            Class clazz = ReflectionKit.getSuperClassGenericType(v.getClass(), 0);
            dictionaryServiceClassNameMap.put(clazz.getSimpleName(), v);
        });
    }

    /**
     * 字典刷新
     * @param dictionaryConst 字典常量名称
     */
    public void refresh(String dictionaryConst) {
        dictionaryRedisTemplate.delete(dictionaryConst);
        dictionaryServiceNameMap.get(dictionaryConst).dataToRedis();
    }

    /**
     * 全部刷新
     */
    public void refreshAll() {
        dictionaryServiceNameMap.forEach((k, v) -> {
            refresh(k);
        });
    }

    /**
     * 根据 redis key 常量获取字典服务
     * @param dictionaryConst
     * @return
     */
    public static DictionaryService service(String dictionaryConst) {
        return dictionaryServiceNameMap.get(dictionaryConst);
    }

    /**
     * 根据类名获取字典服务
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> DictionaryService service(Class<T> clazz) {
        return dictionaryServiceClassNameMap.get(clazz.getSimpleName());
    }
}
