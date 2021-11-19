package com.jcloud.admin.service.impl;

import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.consts.PrivilegesType;
import com.jcloud.admin.entity.ClientApiPrivileges;
import com.jcloud.admin.mapper.ClientPrivilegesMapper;
import com.jcloud.admin.mapper.ClientRoleMapper;
import com.jcloud.admin.service.ClientDetailsService;
import com.jcloud.admin.service.PrivilegesCacheService;
import com.jcloud.admin.service.PrivilegesService;
import com.jcloud.common.bean.ApiLimit;
import com.jcloud.common.consts.Const;
import com.jcloud.common.util.BooleanUtil;
import com.jcloud.orm.service.DefaultOrmService;
import com.jcloud.redis.component.RedisService;
import com.jcloud.swagger.SwaggerResourceBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiaxm
 * @date 2021/9/3
 */
@Service
public class ClientPrivilegesServiceImpl extends DefaultOrmService<ClientPrivilegesMapper, ClientApiPrivileges, ClientApiPrivileges> implements PrivilegesService {

    @Autowired
    private RemoteSwaggerService remoteSwaggerService;

    @Autowired
    private MergePrivileges mergePrivileges;

    @Autowired
    private ClientRoleMapper clientRoleMapper;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PrivilegesCacheService cacheService;

    @Override
    public void save(PrivilegesSaveBean bean) {
        baseMapper.deleteByClientId(bean.getId());
        for (ApiLimit labelNode : bean.getApiConfig()) {
            ClientApiPrivileges privileges = new ClientApiPrivileges();
            privileges.setClientId(bean.getId());
            privileges.setApiPath(labelNode.getApiPath());
            privileges.setApiLimit(labelNode.getApiLimit());
            privileges.setApiLimitStrategy(labelNode.getApiLimitStrategy());
            privileges.setServiceId(bean.getServiceId());
            insertCommonInfo(privileges);
            baseMapper.insert(privileges);
        }
        // 清除缓存
        cacheService.clearPrivileges(bean.getId(), PrivilegesType.CLIENT);
    }

    @Override
    public PrivilegesSaveBean getPrivilegesByResType(PrivilegeBean bean) {
        List<ClientApiPrivileges> privilegesList = baseMapper.findByClientIdAndServiceId(bean.getId(), StringUtils.defaultIfBlank(bean.getServiceId(), null));
        PrivilegesSaveBean saveBean = new PrivilegesSaveBean();
        saveBean.setId(bean.getId());
        saveBean.setResourceType(bean.getResourceType());
        saveBean.setPrivilegeType(bean.getPrivilegeType());
        List<ApiLimit> labelNodes = privilegesList.stream().map(r -> {
            ApiLimit labelNode = new ApiLimit();
            BeanUtils.copyProperties(r, labelNode);
            return labelNode;
        }).collect(Collectors.toList());
        // 是否返回api树
        if (BooleanUtil.numberToBoolean(bean.getShowApiTree())) {
            SwaggerResourceBean swaggerResourceBean = remoteSwaggerService.getResource(bean.getServiceId());
            saveBean.setResourceIds(remoteSwaggerService.formatApiLimit(labelNodes, swaggerResourceBean));
            saveBean.setApiTree(swaggerResourceBean.getResourceTree());
        } else {
            saveBean.setApiConfig(labelNodes);
        }
        return saveBean;
    }

    @Override
    public boolean supports(Integer privileges) {
        return PrivilegesType.CLIENT == privileges.intValue();
    }

    @Override
    public PrivilegesSaveBean getAllPrivileges(PrivilegeBean bean) {
        bean.setShowApiTree(0);
        PrivilegesSaveBean clientPrivileges = getPrivilegesByResType(bean);
        return mergePrivileges.mergePrivileges(bean, clientPrivileges, clientRoleMapper.findByClientId(bean.getId()));
    }

    @Override
    public int decrApi(String key, String apiPath, String serviceId) {
        ClientApiPrivileges clientApiPrivileges = baseMapper.findApiPrivileges(clientDetailsService.getClientDetails(key).getId(), apiPath, serviceId);
        if (clientApiPrivileges.getApiLimit().intValue() == Integer.MAX_VALUE) {
            return Const.CODE_SUCCESS;
        }
        // 双重检验防止竞争问题
        if (clientApiPrivileges.getApiLimit() > 0) {
            String lockKey = "lock:" + key + apiPath + serviceId;
            if (redisService.casLock(lockKey, StringUtils.EMPTY, 30l)) { // 单线程更新该数据
                clientApiPrivileges = baseMapper.findApiPrivileges(clientDetailsService.getClientDetails(key).getId(), apiPath, serviceId);
                if (clientApiPrivileges.getApiLimit() > 0) {
                    clientApiPrivileges.setApiLimit(clientApiPrivileges.getApiLimit() - 1);
                    baseMapper.updateById(clientApiPrivileges);
                }
                redisService.del(lockKey);
            }
            return Const.CODE_SUCCESS;
        }
        return Const.CODE_ERROR;
    }

    @Override
    public ClientApiPrivileges convert(ClientApiPrivileges clientApiPrivileges) {
        return clientApiPrivileges;
    }
}
