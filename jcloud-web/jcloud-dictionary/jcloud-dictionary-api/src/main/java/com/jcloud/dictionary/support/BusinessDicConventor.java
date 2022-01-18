package com.jcloud.dictionary.support;

import com.jcloud.common.util.HBeanUtils;

import java.util.function.Function;

/**
 * 通用字段转换
 *
 * @author jiaxm
 * @date 2021/9/10
 */
public class BusinessDicConventor<T, V> extends DictionaryBeanTransFormer {


    private Class voClass;

    /**
     * 忽略的参数，可以为null
     */
    private String[] ignoreProperties;

    /**
     * 包含的参数，可以为null
     */
    private String[] includeProperties;

    private Function convertFunction;

    public BusinessDicConventor(Class clazz, String[] includeProperties, String[] ignoreProperties) {
        this.voClass = clazz;
        this.ignoreProperties = ignoreProperties;
        this.includeProperties = includeProperties;
    }

    public BusinessDicConventor(Class clazz, String[] includeProperties, String[] ignoreProperties, Function<T, V> convertFunction) {
        this.voClass = clazz;
        this.ignoreProperties = ignoreProperties;
        this.includeProperties = includeProperties;
        this.convertFunction = convertFunction;
    }

    @Override
    protected Object convertToV(Object o) {
        return convertToV(o, convertFunction);
    }

    public Object convertToV(Object o, Function function) {
        try {
            if (function != null) {
                return function.apply(o);
            } else {
                Object objectVo = voClass.newInstance();
                HBeanUtils.copyProperties(o, objectVo, includeProperties, ignoreProperties);
                return objectVo;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class getVoClass() {
        return voClass;
    }
}
