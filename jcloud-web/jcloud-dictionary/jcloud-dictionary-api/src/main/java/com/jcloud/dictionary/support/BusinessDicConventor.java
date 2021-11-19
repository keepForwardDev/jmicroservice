package com.jcloud.dictionary.support;

import com.jcloud.common.util.HBeanUtils;

/**
 * 通用字段转换
 * @author jiaxm
 * @date 2021/9/10
 */
public class BusinessDicConventor extends DictionaryBeanTransFormer {

    private Class voClass;

    /**
     * 忽略的参数，可以为null
     */
    private String[] ignoreProperties;

    /**
     * 包含的参数，可以为null
     */
    private String[] includeProperties;

    public BusinessDicConventor(Class clazz, String[] includeProperties, String[] ignoreProperties) {
        this.voClass = clazz;
        this.ignoreProperties = ignoreProperties;
        this.includeProperties = includeProperties;
    }

    @Override
    protected Object convertToV(Object o) {
        try {
            Object objectVo = voClass.newInstance();
            HBeanUtils.copyProperties(o, objectVo, includeProperties, ignoreProperties);
            return objectVo;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
