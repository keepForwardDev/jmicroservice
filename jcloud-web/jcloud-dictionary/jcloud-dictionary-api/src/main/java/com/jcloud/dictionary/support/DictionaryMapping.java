package com.jcloud.dictionary.support;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * 字典映射转换， 单个转换可以用 {@link JacksonDicMapping}
 * @see DictionaryBeanTransFormer
 * @see DictionaryMappingUtil
 * @author jiaxm
 * @date 2021/4/30
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictionaryMapping {

    /**
     * bean 中 字典的id字段名称
     * @return
     */
    String value();

    /**
     * 如果是多选的，可以指定分割符,此时Field 为string
     * @return
     */
    String separator() default StringUtils.EMPTY;

    /**
     * 字典常量
     * @return
     */
    String dictionaryConst();
}
