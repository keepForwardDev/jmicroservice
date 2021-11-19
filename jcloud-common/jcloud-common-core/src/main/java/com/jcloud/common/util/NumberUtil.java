package com.jcloud.common.util;

import java.math.BigDecimal;

public class NumberUtil extends cn.hutool.core.util.NumberUtil {

    /**
     * 默认为0
     *
     * @param obj
     * @return
     */
    public static Integer defaultInteger(Integer obj) {
        if (obj == null) {
            return 0;
        }
        return obj;
    }

    /**
     * @param obj
     * @param defaultInteger
     * @return
     */
    public static Integer defaultInteger(Integer obj, Integer defaultInteger) {
        if (obj == null) {
            return defaultInteger;
        }
        return obj;
    }

    /**
     * 四舍五入Double
     *
     * @param value
     * @return
     */
    public static Double halfUpDouble(Double value) {
        if (value == null) {
            return null;
        }
        BigDecimal dec = new BigDecimal(value.toString());
        return dec.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static <T> Object defaultNumber(Class<T> clazz, Object value) {
        if (value != null) {
            return value;
        }
        if (clazz.equals(Double.class)) {
            return 0d;
        } else if (clazz.equals(Long.class)) {
            return 0l;
        } else if (clazz.equals(Integer.class)) {
            return 0;
        }
        return null;
    }

    public static Integer booleanToInteger(boolean value) {
        if (value) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Integer booleanToInteger(String value) {
        return booleanToInteger(BooleanUtil.stringToBoolean(value));
    }
}
