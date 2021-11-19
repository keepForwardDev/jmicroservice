package com.jcloud.orm.multidatasource;

import java.lang.annotation.*;

/**
 * 多数据源 支持，在mapper类上或者方法上添加该注解，方法上的级别大于类上的
 * 使用时只需要在spring中注入 DataSource,在mapper上添加该注解，
 *
 * 可以按照包区分，在包中创建package-info.java,在package 上加入该注解从而无需所有类都加上该注解
 *
 * 如何使用？将DataSource 交给spring容器管理，然后在mapper注解上添加这个注解，value 写 注入 IOC容器的BEAN NAME
 *
 *
 * @author jiaxm
 * @date 2021/5/11
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Routing {

    /**
     * the name of ioc dataSource
     * @return
     */
    String value();
}
