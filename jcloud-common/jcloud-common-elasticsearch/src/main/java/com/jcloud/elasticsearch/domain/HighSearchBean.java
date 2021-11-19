package com.jcloud.elasticsearch.domain;

import lombok.Data;

@Data
public class HighSearchBean {

    public String fieldName;
    /**
     * 字段属性
     */
    public String field;

    /**
     * 字段类型
     */
    public String fieldType;

    /**
     * 符号
     */
    public String symbol;
    /**
     * 字段属性值
     */
    public String value;

    /**
     * 条件  and 或 or
     */
    public String condition;


    public HighSearchBean(String field, String fieldType, String symbol, String value, String condition) {
        this.field = field;
        this.fieldType = fieldType;
        this.symbol = symbol;
        this.value = value;
        this.condition = condition;
    }

    public HighSearchBean() {
    }

    public HighSearchBean(String fieldName, String field) {
        this.fieldName = fieldName;
        this.field = field;
    }
}
