package com.jcloud.admin.service.impl;

import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.consts.PrivilegesType;
import com.jcloud.admin.consts.ResType;
import com.jcloud.admin.entity.RolePrivileges;
import com.jcloud.admin.mapper.RolePrivilegesMapper;
import com.jcloud.admin.service.PrivilegesCacheService;
import com.jcloud.admin.service.PrivilegesService;
import com.jcloud.common.bean.ApiLimit;
import com.jcloud.common.util.BooleanUtil;
import com.jcloud.orm.service.DefaultOrmService;
import com.jcloud.swagger.SwaggerResourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Service
public class RolePrivilegesServiceImpl extends DefaultOrmService<RolePrivilegesMapper, RolePrivileges, RolePrivileges> implements PrivilegesService {

    @Autowired
    private RemoteSwaggerService remoteSwaggerService;

    @Autowired
    private PrivilegesCacheService cacheService;

    @Override
    public void save(PrivilegesSaveBean bean) {
        // 删除后新增
        baseMapper.deleteRolePrivileges(bean.getId(), bean.getResourceType(), bean.getServiceId());
        if (bean.getResourceType() == ResType.API) {
            for (ApiLimit labelNode : bean.getApiConfig()) {
                RolePrivileges rolePrivileges = new RolePrivileges();
                rolePrivileges.setRoleId(bean.getId());
                rolePrivileges.setResourceType(bean.getResourceType());
                rolePrivileges.setApiPath(labelNode.getApiPath());
                rolePrivileges.setApiLimit(labelNode.getApiLimit());
                rolePrivileges.setApiLimitStrategy(labelNode.getApiLimitStrategy());
                rolePrivileges.setServiceId(bean.getServiceId());
                insertCommonInfo(rolePrivileges);
                baseMapper.insert(rolePrivileges);
            }
        } else {
            bean.getResourceIds().stream().distinct().forEach(r -> {
                RolePrivileges rolePrivileges = new RolePrivileges();
                rolePrivileges.setRoleId(bean.getId());
                rolePrivileges.setResourceType(bean.getResourceType());
                rolePrivileges.setRelateId(r);
                insertCommonInfo(rolePrivileges);
                baseMapper.insert(rolePrivileges);
            });
        }
        // 清除缓存
        cacheService.clearPrivileges(bean.getId(), PrivilegesType.ROLE);
    }

    @Override
    public PrivilegesSaveBean getPrivilegesByResType(PrivilegeBean privilegeBean) {
        List<RolePrivileges> rolePrivilegesList = baseMapper.findByRoleIdAndResType(privilegeBean.getId(), privilegeBean.getResourceType(), privilegeBean.getServiceId());
        PrivilegesSaveBean bean = new PrivilegesSaveBean();
        bean.setId(privilegeBean.getId());
        bean.setResourceType(privilegeBean.getResourceType());
        bean.setPrivilegeType(privilegeBean.getPrivilegeType());
        if (privilegeBean.getResourceType().intValue() == ResType.API) {
            List<ApiLimit> apiLimits = rolePrivilegesList.stream().map(r -> {
                ApiLimit apiLimit = new ApiLimit();
                apiLimit.setApiLimit(r.getApiLimit());
                apiLimit.setApiPath(r.getApiPath());
                apiLimit.setApiLimitStrategy(r.getApiLimitStrategy());
                return apiLimit;
            }).collect(Collectors.toList());
            // 是否返回api树
            if (BooleanUtil.numberToBoolean(privilegeBean.getShowApiTree())) {
                SwaggerResourceBean swaggerResourceBean = remoteSwaggerService.getResource(privilegeBean.getServiceId());
                bean.setResourceIds(remoteSwaggerService.formatApiLimit(apiLimits, swaggerResourceBean));
                bean.setApiTree(swaggerResourceBean.getResourceTree());
            } else {
                bean.setApiConfig(apiLimits);
            }
        } else {
            bean.setResourceIds(rolePrivilegesList.stream().map(r -> r.getRelateId()).distinct().collect(Collectors.toList()));
        }
        return bean;
    }

    @Override
    public boolean supports(Integer privileges) {
        return privileges.intValue() == PrivilegesType.ROLE;
    }

    @Override
    public PrivilegesSaveBean getAllPrivileges(PrivilegeBean bean) {
        bean.setShowApiTree(0);
        return getPrivilegesByResType(bean);
    }

    @Override
    public int decrApi(String key, String apiPath, String serviceId) {
        return 0;
    }

    @Override
    public RolePrivileges convert(RolePrivileges rolePrivileges) {
        return rolePrivileges;
    }
}
