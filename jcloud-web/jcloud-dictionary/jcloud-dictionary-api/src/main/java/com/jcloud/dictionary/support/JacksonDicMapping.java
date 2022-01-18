package com.jcloud.dictionary.support;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * 字典转换器注解，添加到field上，在jackson 序列化时会输出转换的名称
 * 适用于列表时，转换效率不高，主要性能消耗在io上
 * <p>
 * 多个输出可以用{@link DictionaryMapping} 并且使用{@link DictionaryBeanTransFormer} 转换
 *
 * @author jiaxm
 * @date 2021/4/29
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JacksonDicMapping {

    /**
     * 字典常量
     *
     * @return
     */
    String dictionaryConst();

    /**
     * 转换后字段输出名称，默认字段名+Str
     *
     * @return
     */
    String fieldName() default "";

    /**
     * 是否输出该 id 字段
     *
     * @return
     */
    boolean useId() default true;

    /**
     * 如果是多选的，可以指定分割符,此时Field 为string
     *
     * @return
     */
    String separator() default StringUtils.EMPTY;
}
