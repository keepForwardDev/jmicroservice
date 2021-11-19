package com.alibaba.csp.sentinel.dashboard.rule.gateway;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.util.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/4/19
 */
@Component
public class GatewayFlowRuleProvider implements DynamicRuleProvider<List<GatewayFlowRuleEntity>> {

    @Autowired
    private NacosConfigManager nacosConfigManager;
    @Autowired
    private Converter<String, List<GatewayFlowRuleEntity>> converter;

    @Override
    public List<GatewayFlowRuleEntity> getRules(String appName) throws Exception {
        String rules = nacosConfigManager.getConfigService().getConfig(appName + NacosConfigUtil.GATEWAY_DATA_ID_POSTFIX,
                NacosConfigUtil.GROUP_ID, 3000);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        List<GatewayFlowRuleEntity> list = converter.convert(rules);
        return list;
    }
}
