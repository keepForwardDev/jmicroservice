package com.jcloud.admin.service;

import com.jcloud.admin.bean.PrivilegesSaveBean;

/**
 * 权限缓存
 * @author jiaxm
 * @date 2021/11/3
 */
public interface PrivilegesCacheService {

    String PRIVILEGE_CACHE_KEY = "apiPrivileges:";

    String API_LIMIT_CACHE_KEY = "apiLimit:";
    /**
     * 缓存权限
     * @param key
     * @param privilegesSaveBean
     * @return
     */
    public boolean cachePrivilegesIfAbsent(String key, PrivilegesSaveBean privilegesSaveBean);

    /**
     *
     * @param id 需要清楚缓存的权限承载类型id
     * @param privilegesType 权限承载类型 {@see com.jcloud.admin.consts.ResType}
     * @return
     */
    public boolean clearPrivileges(Long id, Integer privilegesType);

}
