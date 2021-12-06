package com.test;

import org.elasticsearch.common.unit.TimeValue;

/**
 * @author jiaxm
 * @date 2021/12/6
 */
public class ApiTest {
    public static void main(String[] args) {
        TimeValue timeValue = TimeValue.timeValueMillis(60000);
        System.out.println(timeValue.getMinutes());
    }
}
