package com.jcloud.admin.service;

import com.jcloud.admin.bean.DepartmentBean;
import com.jcloud.admin.entity.Department;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.service.CrudListService;

import java.util.List;

public interface DepartmentService extends CrudListService<Department, DepartmentBean> {

    public List<TreeNode> getDepartmentTree();

    public DepartmentBean getInfo(Long id);

    /**
     * 查询部门列表信息
     * @return
     */
	ResponseData listDepartment();

}
