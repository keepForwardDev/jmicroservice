package com.jcloud.elasticsearch.consts;

/**
 * @Description 高阶查询字符
 */
public enum HighSearchChar {
    EQUAL("equal","="),
    NOT_EQUAL("not_equal","!="),
    RANGE_GTE("range_gte",">="),
    RANGE_GT("range_gt",">"),
    RANGE_LTE("range_lte","<="),
    RANGE_LT("range_lt","<"),
    INCLUDE("include","包含"),
    NOT_INCLUDE("notInclude","不包含");
//    SYMBOL_MUST("must","并且"),
//    SHOULD("should","或者");

    private String name;
    private String value;
    HighSearchChar(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
