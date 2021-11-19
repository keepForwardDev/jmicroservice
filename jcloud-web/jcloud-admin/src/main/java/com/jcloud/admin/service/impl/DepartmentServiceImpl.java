package com.jcloud.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jcloud.admin.bean.DepartmentBean;
import com.jcloud.admin.entity.Department;
import com.jcloud.admin.mapper.DepartmentMapper;
import com.jcloud.admin.service.DepartmentService;
import com.jcloud.admin.service.UserService;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.TreeNodeUtil;
import com.jcloud.orm.service.DefaultOrmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl extends DefaultOrmService<DepartmentMapper, Department, DepartmentBean> implements DepartmentService {

    @Autowired
    private UserService userService;


    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public DepartmentBean convert(Department department) {
        DepartmentBean departmentBean = new DepartmentBean();
        BeanUtils.copyProperties(department, departmentBean);
        if (department.getParentId() != null) {
            departmentBean.setParentName(getById(department.getParentId()).getName());
        }
        return departmentBean;
    }

    protected List<String> getIgnoreProperties() {
        List<String> IGNORE_FIELDS = super.getIgnoreProperties();
        IGNORE_FIELDS.add("levelCode");
        return IGNORE_FIELDS;
    }

    @Override
    public QueryWrapper<Department> queryCondition(DepartmentBean bean) {
        QueryWrapper<Department> condition = super.queryCondition(bean);
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.like("name",bean.getName());
        }
        return condition;
    }

    @Override
    public Department insertEntity(DepartmentBean bean) {
        Department entity = new Department();
        BeanUtils.copyProperties(bean, entity, getIgnoreProperties().toArray(new String[]{}));
        insertCommonInfo(entity);
        beforeSave(entity, bean);
        baseMapper.insert(entity);
        afterSave(entity, bean);
        return entity;
    }

    @Override
    protected void afterSave(Department entity, DepartmentBean bean) {
        // 维护levelCode
        threadPoolTaskExecutor.execute(() -> {
            flushLevelCodeAndFullName();
        });
    }


    public void flushLevelCodeAndFullName() {
        int startCode = 1;
        List<TreeNode> treeNodeList = getDepartmentTree();
        for (TreeNode treeNode : treeNodeList) {
            cycleUpdateLevelCodeAndFullName(treeNode, StringUtils.EMPTY, StringUtils.EMPTY, startCode );
            startCode++;
        }
    }

    /**
     *
     * 循环更新level code 和 fullName
     * @param treeNode 需要更新的节点
     * @param parentCode 父级code
     * @param parentName 父级名称
     * @param startCode 子节点个数
     */
    private void cycleUpdateLevelCodeAndFullName(TreeNode treeNode, String parentCode, String parentName, int startCode) {
        parentCode = parentCode + String.format("%03d", startCode);
        parentName = parentName + StringPool.DASH +treeNode.getLabel();
        baseMapper.updateLevelCodeAndFullName(treeNode.getId(), parentCode, parentName);
        if (!CollectionUtil.isEmpty(treeNode.getChildren())) {
            int index = 1;
            for (TreeNode child : treeNode.getChildren()) {
                cycleUpdateLevelCodeAndFullName(child, parentCode, parentName, index);
                index++;
            }
        }
    }

    /**
     * 全部部门树
     *
     * @return
     */
    public List<TreeNode> getDepartmentTree() {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort", "id");
        List<Department> departmentList = baseMapper.selectList(queryWrapper);
        return TreeNodeUtil.buildTree(departmentList, "name", false, 0l);
    }

    @Override
    public DepartmentBean getInfo(Long id) {
        Department department = getById(id);
        DepartmentBean bean = convert(department);
        return bean;
    }



    @Override
    public ResponseData listDepartment() {
        List<Department> departments = departmentMapper.selectList(null);
        return new ResponseData(Const.CODE_SUCCESS, Const.CODE_SUCCESS_STR,departments);
    }

    @Override
    public void physicsDelete(Long id) { // 需要删除下级
        Department department = baseMapper.selectById(id);
        String levelCode = department.getLevelCode();
        QueryWrapper<Department> queryWrapper = new QueryWrapper();
        queryWrapper.likeRight("level_code", levelCode);
        baseMapper.delete(queryWrapper);
    }
}
