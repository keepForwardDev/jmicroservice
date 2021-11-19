package com.jcloud.admin.service;

import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
public interface PrivilegesService {

    /**
     * 保存
     * @param bean
     */
    public void save(PrivilegesSaveBean bean);

    /**
     * 获取资源,获取的是自己承载的权限
     * @param bean
     * @return
     */
    public PrivilegesSaveBean getPrivilegesByResType(PrivilegeBean bean);

    /**
     * 权限承载类型
     * @param privilegeType
     * @return
     */
    public boolean supports(Integer privilegeType);

    /**
     * 获取的是全部承载权限
     * @param bean
     * @return
     */
    public PrivilegesSaveBean getAllPrivileges(PrivilegeBean bean);


    /**
     * 扣除api 次数
     * @param key
     * @param apiPath
     * @param serviceId
     * @return
     */
    public int decrApi(String key, String apiPath, String serviceId);

}
