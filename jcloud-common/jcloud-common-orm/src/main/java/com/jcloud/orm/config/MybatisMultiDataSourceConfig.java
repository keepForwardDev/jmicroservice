package com.jcloud.orm.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.jcloud.orm.multidatasource.RoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * mybatis 多数据源配置，注意其他数据源莫要使用jpa自动建表
 * @author jiaxm
 * @date 2021/5/11
 */
@Configuration
@Slf4j
public class MybatisMultiDataSourceConfig implements InitializingBean {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @Autowired
    private Map<String, DataSource> dataSourceMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSourceMap != null && dataSourceMap.size() > 1) { // 配置了多个数据源的时候使用
            RoutingDataSource routingDataSource = new RoutingDataSource();
            routingDataSource.setDefaultTargetDataSource("dataSource");
            routingDataSource.setTargetDataSources(dataSourceMap);
            Environment environment = sqlSessionFactory.getConfiguration().getEnvironment();
            sqlSessionFactory.getConfiguration().setEnvironment(new Environment(MybatisSqlSessionFactoryBean.class.getSimpleName(), environment.getTransactionFactory(), routingDataSource));
        }
    }
}
