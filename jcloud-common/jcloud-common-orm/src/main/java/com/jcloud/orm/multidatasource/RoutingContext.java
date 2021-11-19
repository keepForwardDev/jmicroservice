package com.jcloud.orm.multidatasource;

/**
 * 获取当前数据源名称，线程绑定
 * @author jiaxm
 * @date 2021/5/11
 */
public class RoutingContext {
    private static final ThreadLocal<String> DATASOURCES = new ThreadLocal<String>();

    public static void setDatasource(String dataSourceName) {
        DATASOURCES.set(dataSourceName);
    }

    public static String getCurrentDataSource() {
        return DATASOURCES.get();
    }

    public static void clear() {
        DATASOURCES.remove();
    }
}
