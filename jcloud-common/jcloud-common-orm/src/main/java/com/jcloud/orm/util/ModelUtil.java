package com.jcloud.orm.util;

import com.jcloud.common.util.UUIDUtils;
import com.jcloud.orm.model.BaseModel;
import com.jcloud.security.bean.ShiroUser;
import com.jcloud.security.utils.SecurityUtil;

import java.util.Date;

/**
 * @author jiaxm
 * @date 2021/9/10
 */
public class ModelUtil {

    /**
     * 基础设置
     * @param t
     */
    public static void setCommonInfo(BaseModel t) {
        Date now = new Date();
        ShiroUser shiroUser = SecurityUtil.getCurrentUser();
        if (t.getId() != null) {
            t.setUpdateTime(now);
        } else {
            if (shiroUser != null) {
                t.setDepartmentId(shiroUser.getDepartmentId());
                t.setCreateUserId(shiroUser.getId());
            }
            t.setCreateTime(now);
            t.setUuid(UUIDUtils.genUUid());
        }
    }
}
