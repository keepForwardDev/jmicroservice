package com.jcloud.admin.service;

import com.jcloud.admin.bean.RoleBean;
import com.jcloud.admin.entity.Role;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.service.CrudListService;

import java.util.List;

public interface RoleService extends CrudListService<Role, RoleBean> {

    public ResponseData saveRole(RoleBean roleBean);

    public void deleteRole(Long id);

    public List<LabelNode> getLabelNodes();
}
