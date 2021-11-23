# 项目介绍
jmicoservice基于Spring Cloud Alibaba所搭建出来的一套微服务系统，支持比较基础的统一认证授权，使用了`d2-admin` 作为后台管理，可以作为后端的脚手架使用。所有服务均可独立启动，无依赖，架构十分简单，适合新手学习。

# 技术栈
SpringBoot、SpringSecurity、Spring Cloud Alibaba、Sentinel、Seata、MyBatisPlus、Elasticsearch、RabbitMQ、Redis、Hutool、OSS、xxl-job、knife4j.....

# 项目结构
jmicoservice
├─jcloud-auth       -- 认证服务中心
├─jcloud-common     -- 公共模块
│  ├─jcloud-common-core -- 公共核心模块，所有服务都进行依赖
│  ├─jcloud-common-elasticsearch -- elasticsearch 公共模块
│  ├─jcloud-common-mq  -- rabbitmq 公共模块
│  ├─jcloud-common-orm -- 数据库公共模块
│  ├─jcloud-common-redis -- redis 公共模块
│  ├─jcloud-common-seata -- 分布式事务seata 公共模块
│  ├─jcloud-common-security -- 认证公共模块
│  ├─jcloud-common-sentinel -- 限流sentinel公共模块
│  ├─jcloud-common-swagger -- swagger公共模块
│  └─jcloud-common-xxljob -- xxljob公共模块
├─jcloud-gateway -- 网关服务
├─jcloud-remote -- feign 远程接口服务
└─jcloud-web 
    ├─jcloud-admin  -- 后台管理服务
    ├─jcloud-dictionary
    │  ├─jcloud-dictionary-api -- 字典api公共模块
    │  ├─jcloud-dictionary-web -- 字典api服务
    ├─jcloud-schedule-center
    │  ├─jcloud-schedule-task  -- 分布式调度中心
    │  └─xxljob-admin          --  xxljob后台管理
    └─jcloud-sentinel-dashboard  -- 限流sentinel 后台管理

# 项目界面
![avatar](https://www.jaxmine.com/upload/2021/11/system-3534555fafc84e0391f7d2fafecc87dc.png)

# 环境搭建
