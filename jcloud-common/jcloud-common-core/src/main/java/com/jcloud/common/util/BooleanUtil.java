package com.jcloud.common.util;

public class BooleanUtil {


    public static final String YES_CN = "是";

    public static final String NO_CN = "否";

    public static boolean numberToBoolean(Number obj) {
        if (obj==null) {
            return false;
        }
        return obj.intValue()==1;
    }

    /**
     * 中文转换 是否
     * @param flag
     * @return
     */
    public static String booleanString(boolean flag) {
        return flag? YES_CN: NO_CN;
    }


    /**
     * 中文转换是否
     * @param obj
     * @return
     */
    public static String booleanString(Number obj) {
        return booleanString(numberToBoolean(obj));
    }
    public static boolean stringToBoolean(String value) {
        if (YES_CN.equals(value)) {
            return true;
        } else if (Boolean.TRUE.toString().equals(value)) {
            return true;
        } else if ("1".equals(value)) {
            return true;
        }
        return false;
    }


}
