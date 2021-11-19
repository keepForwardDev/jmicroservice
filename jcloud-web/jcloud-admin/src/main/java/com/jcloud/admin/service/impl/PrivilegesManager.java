package com.jcloud.admin.service.impl;

import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.service.PrivilegesCacheService;
import com.jcloud.admin.service.PrivilegesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限管理
 * @author jiaxm
 * @date 2021/9/3
 */
@Service
public class PrivilegesManager {

    @Autowired
    private List<PrivilegesService> privilegesServices;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PrivilegesCacheService privilegesCacheService;

    /**
     * 保存权限
     * @param bean
     */
    public void save(PrivilegesSaveBean bean) {
        PrivilegesService service = chooseService(bean.getPrivilegeType());
        service.save(bean);
    }

    /**
     * 获取权限
     * @param bean
     * @return
     */
    public PrivilegesSaveBean getPrivileges(PrivilegeBean bean) {
        PrivilegesService service = chooseService(bean.getPrivilegeType());
        return service.getPrivilegesByResType(bean);
    }

    /**
     * 获取组合的全部权限
     * @param bean
     * @return
     */
    public PrivilegesSaveBean getAllPrivileges(PrivilegeBean bean) {
        PrivilegesService service = chooseService(bean.getPrivilegeType());
        return service.getAllPrivileges(bean);
    }

    /**
     * 获取支持的service
     * @param privilegeType
     * @return
     */
    public PrivilegesService chooseService(Integer privilegeType) {
        return privilegesServices.stream().filter(r -> r.supports(privilegeType)).findFirst().orElse(null);
    }

    /**
     * 获取api 权限，并设置缓存
     * 缓存权限 apiPrivileges:权限承载类型 0 用户 2应用:客户端ID或者用户手机号:服务名
     * 缓存api次数 apiLimit:权限承载类型 0 用户 2应用:客户端ID或者用户手机号:服务名:请求地址
     * 示例
     * apiPrivileges:2:data-center:jcloud-admin
     * apiLimit:2:data-center:jcloud-admin:/user/checkPhone/{phone}
     * @param key 这里传入的是 权限承载类型 0 用户 2应用:客户端ID或者用户手机号
     * @param bean
     * @return
     */
    public PrivilegesSaveBean getAllPrivileges(String key, PrivilegeBean bean) {
        // 判断key
        String privilegeKey = PrivilegesCacheService.PRIVILEGE_CACHE_KEY + key;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        PrivilegesSaveBean privilegesSaveBean = (PrivilegesSaveBean) valueOperations.get(privilegeKey);
        // 不存在该服务的权限，设置缓存
        if (privilegesSaveBean == null) {
            privilegesSaveBean = getAllPrivileges(bean);
            privilegesCacheService.cachePrivilegesIfAbsent(key, privilegesSaveBean);
        }
        return privilegesSaveBean;
    }

    /**
     * 删减api
     * @param privilegeType 权限承载类型
     * @param key 手机号 或者 应用id
     * @param apiPath
     * @param serviceId
     * @return
     */
    public int decrApi(Integer privilegeType,String key, String apiPath, String serviceId) {
        PrivilegesService service = chooseService(privilegeType);
        return service.decrApi(key, apiPath, serviceId);
    }
}
