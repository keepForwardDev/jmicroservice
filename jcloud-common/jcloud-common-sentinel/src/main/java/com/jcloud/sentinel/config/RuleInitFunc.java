package com.jcloud.sentinel.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.property.PropertyListener;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.jcloud.sentinel.consts.SentinelRuleConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 流控规则保存在nacos 上， 控制台修改数据，发布到nacos，这里配置监听，刷新规则
 *
 * @author jiaxm
 * @date 2021/4/19
 */
@Component
@ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true, havingValue = "true")
public class RuleInitFunc implements InitFunc {

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Value("${spring.application.name}")
    private String applicationName;


    @Override
    public void init() throws Exception {

    }

    /**
     * 网关应用
     *
     * @return
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public NacosDataSource<List<GatewayFlowRule>> gatewayFlowRuleDataSource() {
        NacosDataSource<List<GatewayFlowRule>> gatewayFlowRuleDataSource = new NacosDataSource<>
                (nacosConfigManager, SentinelRuleConst.GROUP_ID, getDataId(SentinelRuleConst.GATEWAY_DATA_ID_POSTFIX), (source) -> {
                    return JSON.parseObject(source, new TypeReference<List<GatewayFlowRule>>() {
                    }, new Feature[0]);
                });
        gatewayFlowRuleDataSource.getProperty().addListener(new PropertyListener<List<GatewayFlowRule>>() {
            @Override
            public void configUpdate(List<GatewayFlowRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                   value = new ArrayList<>();
                }
                Set<GatewayFlowRule> set = new LinkedHashSet<>();
                set.addAll(value);
                GatewayRuleManager.loadRules(set);
            }

            @Override
            public void configLoad(List<GatewayFlowRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                Set<GatewayFlowRule> set = new LinkedHashSet<>();
                set.addAll(value);
                GatewayRuleManager.loadRules(set);
            }
        });
        return gatewayFlowRuleDataSource;
    }

    /**
     * 流控规则
     *
     * @return
     */
    @Bean
    public NacosDataSource<List<FlowRule>> flowRuleDataSource() {
        NacosDataSource<List<FlowRule>> flowRuleDataSource = new NacosDataSource<>
                (nacosConfigManager, SentinelRuleConst.GROUP_ID, getDataId(SentinelRuleConst.FLOW_DATA_ID_POSTFIX), (source) -> {
                    return JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                    }, new Feature[0]);
                });
        flowRuleDataSource.getProperty().addListener(new PropertyListener<List<FlowRule>>() {
            @Override
            public void configUpdate(List<FlowRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                FlowRuleManager.loadRules(value);
            }

            @Override
            public void configLoad(List<FlowRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                FlowRuleManager.loadRules(value);
            }
        });
        return flowRuleDataSource;
    }

    /**
     * 参数规则
     *
     * @return
     */
    @Bean
    public NacosDataSource<List<ParamFlowRule>> paramFlowRule() {
        NacosDataSource<List<ParamFlowRule>> paramFlowRule = new NacosDataSource<>
                (nacosConfigManager, SentinelRuleConst.GROUP_ID, getDataId(SentinelRuleConst.PARAM_FLOW_DATA_ID_POSTFIX), (source) -> {
                    return JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                    }, new Feature[0]);
                });
        paramFlowRule.getProperty().addListener(new PropertyListener<List<ParamFlowRule>>() {
            @Override
            public void configUpdate(List<ParamFlowRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                ParamFlowRuleManager.loadRules(value);
            }

            @Override
            public void configLoad(List<ParamFlowRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                ParamFlowRuleManager.loadRules(value);
            }
        });
        return paramFlowRule;
    }

    /**
     * 降级
     * @return
     */
    @Bean
    public NacosDataSource<List<DegradeRule>> degradeRule() {
        NacosDataSource<List<DegradeRule>> degradeRule = new NacosDataSource<>
                (nacosConfigManager, SentinelRuleConst.GROUP_ID, getDataId(SentinelRuleConst.DEGRADE_DATA_ID_POSTFIX), (source) -> {
                    return JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                    }, new Feature[0]);
                });
        degradeRule.getProperty().addListener(new PropertyListener<List<DegradeRule>>() {
            @Override
            public void configUpdate(List<DegradeRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                DegradeRuleManager.loadRules(value);
            }

            @Override
            public void configLoad(List<DegradeRule> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                DegradeRuleManager.loadRules(value);
            }
        });
        return degradeRule;
    }


    /**
     * 网关api分组
     * @return
     */
    @Bean
    public NacosDataSource<List<ApiDefinition>> apiDefinitionGroup() {
        NacosDataSource<List<ApiDefinition>> apiDefinitionGroup = new NacosDataSource<>
                (nacosConfigManager, SentinelRuleConst.GROUP_ID, getDataId(SentinelRuleConst.GATEWAY_API_DATA_ID_POSTFIX), (source) -> {
                   List<Map<String, Object>> list = JSON.parseObject(source, new TypeReference<List<Map<String, Object>>>() {
                   }, new Feature[0]);
                   if (!CollectionUtils.isEmpty(list)) {
                       List<ApiDefinition> apiDefinitions = new ArrayList<>();
                       for (Map<String, Object> stringObjectMap : list) {
                           ApiDefinition apiDefinition = new ApiDefinition();
                           apiDefinition.setApiName(stringObjectMap.get("apiName").toString());
                           JSONArray jsonArray = (JSONArray) stringObjectMap.get("predicateItems");
                           List<ApiPathPredicateItem> apiPathPredicateItems = jsonArray.toJavaList(ApiPathPredicateItem.class);
                           Set<ApiPredicateItem> set = new LinkedHashSet<>();
                           set.addAll(apiPathPredicateItems);
                           apiDefinition.setPredicateItems(set);
                           apiDefinitions.add(apiDefinition);
                       }
                       return apiDefinitions;
                   }
                   return Collections.emptyList();
                });
        apiDefinitionGroup.getProperty().addListener(new PropertyListener<List<ApiDefinition>>() {
            @Override
            public void configUpdate(List<ApiDefinition> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                Set<ApiDefinition> set = new LinkedHashSet<>();
                set.addAll(value);
                GatewayApiDefinitionManager.loadApiDefinitions(set);
            }

            @Override
            public void configLoad(List<ApiDefinition> value) {
                if (CollectionUtils.isEmpty(value)) {
                    value = new ArrayList<>();
                }
                Set<ApiDefinition> set = new LinkedHashSet<>();
                set.addAll(value);
                GatewayApiDefinitionManager.loadApiDefinitions(set);
            }
        });
        return apiDefinitionGroup;
    }


    private String getDataId(String dataId) {
        return applicationName + dataId;
    }
}
