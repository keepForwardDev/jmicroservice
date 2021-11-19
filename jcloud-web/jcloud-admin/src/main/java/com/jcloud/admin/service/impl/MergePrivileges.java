package com.jcloud.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.consts.PrivilegesType;
import com.jcloud.admin.consts.ResType;
import com.jcloud.admin.entity.Role;
import com.jcloud.common.bean.ApiLimit;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限合并
 * @author jiaxm
 * @date 2021/9/8
 */
@Service
public class MergePrivileges {

    @Autowired
    private PrivilegesManager privilegesManager;


    public PrivilegesSaveBean mergePrivileges(PrivilegeBean bean, PrivilegesSaveBean personalPrivileges, List<Role> roles) {
        List<PrivilegesSaveBean> rolePrivileges = new ArrayList<>();
        if (!CollectionUtil.isEmpty(roles)) {
            PrivilegeBean privilegeBean = new PrivilegeBean();
            privilegeBean.setPrivilegeType(PrivilegesType.ROLE);
            privilegeBean.setResourceType(bean.getResourceType());
            privilegeBean.setShowApiTree(0);
            privilegeBean.setServiceId(bean.getServiceId());
            for (Role userRole : roles) {
                privilegeBean.setId(userRole.getId());
                PrivilegesSaveBean privilegesSaveBean1 = privilegesManager.getPrivileges(privilegeBean);
                rolePrivileges.add(privilegesSaveBean1);
            }
            return mergePrivileges(personalPrivileges, rolePrivileges);
        }
        return personalPrivileges;
    }

    /**
     * 合并规则，按照次数配置谁大 替换谁
     * @param personalPrivileges
     * @param rolePrivileges
     * @return
     */
    private PrivilegesSaveBean mergePrivileges(PrivilegesSaveBean personalPrivileges, List<PrivilegesSaveBean> rolePrivileges) {
        if (CollectionUtil.isEmpty(rolePrivileges)) {
            return personalPrivileges;
        }
        if (personalPrivileges.getResourceType().intValue() == ResType.API) { // 合并api
            List<ApiLimit> apiLimits = new ArrayList<>();
            for (PrivilegesSaveBean rolePrivilege : rolePrivileges) {
                apiLimits.addAll(rolePrivilege.getApiConfig());
            }
            // 合并
            // 以调用次数为大 合并
            Map<String, ApiLimit> otherApiMap = apiLimits.stream().collect(Collectors.toMap(r -> r.getApiPath() + r.getServiceId(), r -> r, (old, cur) -> {
                if (old.getApiLimit() >= cur.getApiLimit()) {
                    return old;
                }
                return cur;
            }));
            // 个人 > 角色
            Map<String, ApiLimit> personalApiMap = personalPrivileges.getApiConfig().stream().collect(Collectors.toMap(r -> r.getApiPath() + r.getServiceId(), r -> r));
            otherApiMap.forEach((k , v) -> {
                ApiLimit apiLimit = personalApiMap.get(k);
                if (apiLimit == null) {
                    personalPrivileges.getApiConfig().add(v);
                }
            });
        } else {
            rolePrivileges.forEach(r -> {
                personalPrivileges.getResourceIds().addAll(r.getResourceIds());
            });
            personalPrivileges.setResourceIds(personalPrivileges.getResourceIds().stream().distinct().collect(Collectors.toList()));
        }
        return personalPrivileges;
    }


    /**
     * 前端采用树形结构展示使用方法
     * @param personalPrivileges
     * @param rolePrivileges
     * @return
     */
    private PrivilegesSaveBean mergeTreePrivileges(PrivilegesSaveBean personalPrivileges, List<PrivilegesSaveBean> rolePrivileges) {
        if (CollectionUtil.isEmpty(rolePrivileges)) {
            return personalPrivileges;
        }
        if (personalPrivileges.getResourceType().intValue() == ResType.API) {
            Map<Object, TreeNode> pathMap = TreeNodeUtil.treeToMap(personalPrivileges.getApiTree(), ReflectionUtils.findField(TreeNode.class, "extra"), new HashMap<>());
            List<ApiLimit> apiLimits = new ArrayList<>();
            for (PrivilegesSaveBean rolePrivilege : rolePrivileges) {
                apiLimits.addAll(rolePrivilege.getApiConfig());
            }
            // 以调用次数为大 合并
            Map<String, ApiLimit> apiLimitMap = apiLimits.stream().collect(Collectors.toMap(r -> r.getApiPath(), r -> r, (old, cur) -> {
                if (old.getApiLimit() >= cur.getApiLimit()) {
                    return old;
                }
                return cur;
            }));
            mergeApiPrivileges(pathMap, personalPrivileges, apiLimitMap.values());
        } else {
            rolePrivileges.forEach(r -> {
                personalPrivileges.getResourceIds().addAll(r.getResourceIds());
            });
            personalPrivileges.setResourceIds(personalPrivileges.getResourceIds().stream().distinct().collect(Collectors.toList()));
        }
        return personalPrivileges;
    }

    /**
     * 合并api权限
     *
     * @param pathMap
     * @param personalPrivilege
     * @param apiLimits
     */
    private void mergeApiPrivileges(Map<Object, TreeNode> pathMap, PrivilegesSaveBean personalPrivilege, Collection<ApiLimit> apiLimits) {
        for (ApiLimit apiLimit : apiLimits) {
            TreeNode treeNode = pathMap.get(apiLimit.getApiPath());
            if (treeNode == null || personalPrivilege.getResourceIds().contains(treeNode.getId())) { // 个人 > 角色
                continue;
            } else {
                ApiLimit apiLimit1 = (ApiLimit) treeNode.getData();
                apiLimit1.setApiLimitStrategy(apiLimit.getApiLimitStrategy());
                apiLimit1.setApiLimit(apiLimit.getApiLimit());
                personalPrivilege.getResourceIds().add(treeNode.getId());
            }
        }
    }

}
