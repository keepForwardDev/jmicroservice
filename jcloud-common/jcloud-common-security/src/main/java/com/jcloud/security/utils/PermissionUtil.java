package com.jcloud.security.utils;

import com.jcloud.security.bean.ShiroUser;
import com.jcloud.security.consts.RoleConst;

public class PermissionUtil {

    /**
     * 是否是系统管理员
     *
     * @param user
     * @return
     */
    public static boolean hasSuperAdmin(ShiroUser user) {
        return hasRole(user, RoleConst.SUPER_ADMIN);
    }

    /**
     * 是否是区域管理员
     *
     * @param user
     * @return
     */
    public static boolean hasAreaManager(ShiroUser user) {
        return hasRole(user, RoleConst.AREA_ADMIN);
    }

    /**
     * 是否是产业合伙人
     * @param user
     * @return
     */
    public static boolean hasDomainLinkManager(ShiroUser user) {
        return hasRole(user, RoleConst.DOMAIN_LINK_MANAGER);
    }

    public static boolean hasRole(ShiroUser user, String role) {
        if (user == null) {
            return false;
        }
        return user.getRolesCode().contains(role);
    }
}
