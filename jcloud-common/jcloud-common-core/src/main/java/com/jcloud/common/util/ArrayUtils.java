package com.jcloud.common.util;

import java.util.List;

/**
 * 数组工具
 *
 * @author jiaxm
 * @date 2021/2/19
 */
public class ArrayUtils {

    /**
     * list Long 转为基本类型 long数组
     *
     * @param list
     * @return
     */
    public static long[] listLongToArray(List<Long> list) {
        long[] array = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
