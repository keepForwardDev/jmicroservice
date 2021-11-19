package com.jcloud.admin.service;

import com.jcloud.admin.bean.AppDetail;
import com.jcloud.admin.entity.ClientDetails;
import com.jcloud.orm.service.CrudListService;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
public interface ClientDetailsService extends CrudListService<ClientDetails, AppDetail> {

    /**
     * 根據clientId获取
     * @param clientId
     * @return
     */
    public ClientDetails getClientDetails(String clientId);
}
