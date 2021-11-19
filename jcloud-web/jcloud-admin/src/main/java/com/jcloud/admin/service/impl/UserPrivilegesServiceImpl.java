package com.jcloud.admin.service.impl;

import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.consts.PrivilegesType;
import com.jcloud.admin.consts.ResType;
import com.jcloud.admin.entity.Role;
import com.jcloud.admin.entity.UserPrivileges;
import com.jcloud.admin.mapper.UserMapper;
import com.jcloud.admin.mapper.UserPrivilegesMapper;
import com.jcloud.admin.mapper.UserRoleMapper;
import com.jcloud.admin.service.PrivilegesCacheService;
import com.jcloud.admin.service.PrivilegesService;
import com.jcloud.admin.service.UserService;
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
public class UserPrivilegesServiceImpl extends DefaultOrmService<UserPrivilegesMapper, UserPrivileges, UserPrivileges> implements PrivilegesService {

    @Autowired
    private RemoteSwaggerService remoteSwaggerService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MergePrivileges mergePrivileges;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PrivilegesCacheService cacheService;

    @Override
    public void save(PrivilegesSaveBean bean) {
        baseMapper.deleteByUserIdAndResourceType(bean.getId(), bean.getResourceType(), bean.getServiceId());
        if (bean.getResourceType() == ResType.API) {
            for (ApiLimit labelNode : bean.getApiConfig()) {
                UserPrivileges privileges = new UserPrivileges();
                privileges.setUserId(bean.getId());
                privileges.setResourceType(bean.getResourceType());
                privileges.setApiPath(labelNode.getApiPath());
                privileges.setApiLimit(labelNode.getApiLimit());
                privileges.setApiLimitStrategy(labelNode.getApiLimitStrategy());
                privileges.setServiceId(bean.getServiceId());
                insertCommonInfo(privileges);
                baseMapper.insert(privileges);
            }
        } else {
            bean.getResourceIds().stream().distinct().forEach(r -> {
                UserPrivileges privileges = new UserPrivileges();
                privileges.setUserId(bean.getId());
                privileges.setResourceType(bean.getResourceType());
                privileges.setRelateId(r);
                insertCommonInfo(privileges);
                baseMapper.insert(privileges);
            });
        }
        // 清除缓存
        cacheService.clearPrivileges(bean.getId(), PrivilegesType.USER);
    }

    @Override
    public PrivilegesSaveBean getPrivilegesByResType(PrivilegeBean bean) {
        List<UserPrivileges> privilegesList = baseMapper.findByUserIdAndResourceTypeAndServiceId(bean.getId(), bean.getResourceType(), StringUtils.defaultIfBlank(bean.getServiceId(), null));
        PrivilegesSaveBean saveBean = new PrivilegesSaveBean();
        saveBean.setId(bean.getId());
        saveBean.setResourceType(bean.getResourceType());
        saveBean.setPrivilegeType(bean.getPrivilegeType());
        if (bean.getResourceType().intValue() == ResType.API) {
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
        } else {
            saveBean.setResourceIds(privilegesList.stream().map(r -> r.getRelateId()).distinct().collect(Collectors.toList()));
        }
        return saveBean;
    }

    @Override
    public boolean supports(Integer privileges) {
        return privileges.intValue() == PrivilegesType.USER;
    }

    /**
     * 用户权限等于个人权限 + 角色权限
     * 个人权限设置 > 角色权限设置
     *
     * @param bean
     * @return
     */
    @Override
    public PrivilegesSaveBean getAllPrivileges(PrivilegeBean bean) {
        bean.setShowApiTree(0);
        // 个人权限
        PrivilegesSaveBean privilegesSaveBean = getPrivilegesByResType(bean);
        // 查看角色权限
        List<Role> userRoles = userRoleMapper.findByUserId(bean.getId());
        return mergePrivileges.mergePrivileges(bean, privilegesSaveBean, userRoles);
    }

    @Override
    public int decrApi(String key, String apiPath, String serviceId) {
        UserPrivileges userPrivileges = baseMapper.findApiPrivileges(userService.getUserIdByPhone(key), apiPath, serviceId);
        if (userPrivileges.getApiLimit().intValue() == Integer.MAX_VALUE) {
            return Const.CODE_SUCCESS;
        }
        // 双重检验防止竞争问题
        if (userPrivileges.getApiLimit() > 0) {
            String lockKey = "lock:" + key + apiPath + serviceId;
            if (redisService.casLock(lockKey, StringUtils.EMPTY, 30l)) { // 单线程更新该数据
                userPrivileges = baseMapper.findApiPrivileges(userService.getUserIdByPhone(key), apiPath, serviceId);
                if (userPrivileges.getApiLimit() > 0) {
                    userPrivileges.setApiLimit(userPrivileges.getApiLimit() - 1);
                    baseMapper.updateById(userPrivileges);
                }
                redisService.del(lockKey);
            }
            return Const.CODE_SUCCESS;
        }
        return Const.CODE_ERROR;
    }

    @Override
    public UserPrivileges convert(UserPrivileges userPrivileges) {
        return userPrivileges;
    }
}
