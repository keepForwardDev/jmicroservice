package com.jcloud.common.util;

import org.springframework.util.StringUtils;

public class SqlHelper {
    public final static String DEFAULTESCAPE="/";

    public final static String PERCENT="%";

    public final static String UNDERLINE="_";

    public final static String PREFIX="^";

    public final static String SUFFIX=" escape '/' ";

    /**
     * 后面跟 escape '/',使用预编译
     * @param str
     * @see #getSqlLike
     * @return
     */
    public static String getSqlLikeParams(String str){
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String escaped=str.replace("%", DEFAULTESCAPE+PERCENT).replace("_", DEFAULTESCAPE+UNDERLINE).replace("^", DEFAULTESCAPE+PREFIX);
        return PERCENT+escaped+PERCENT ;
    }

    /**
     *
     * @param key 绑定占位参数名
     * @return
     */
    public static String getBundSqlLike(String key) {
        String sql= "like :"+key;
        return sql+SUFFIX;
    }

    public static String getSqlLike(String columnName) {
        String sql= columnName + " like ? ";
        return sql+SUFFIX;
    }

    public static String getSqlLike(String columnName, int index) {
        String sql= columnName + " like {%d} ";
        return String.format(sql, index) + SUFFIX;
    }
}
