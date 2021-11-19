package com.jcloud.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcloud.admin.bean.RoleBean;
import com.jcloud.admin.entity.Role;
import com.jcloud.admin.mapper.RoleMapper;
import com.jcloud.admin.service.RoleService;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.SqlHelper;
import com.jcloud.orm.service.DefaultOrmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl extends DefaultOrmService<RoleMapper, Role, RoleBean> implements RoleService {


    @Override
    public RoleBean convert(Role role) {
        RoleBean roleBean = new RoleBean();
        BeanUtils.copyProperties(role, roleBean);
        return roleBean;
    }

    /**
     * 角色保存
     * @param roleBean
     */
    public ResponseData saveRole(RoleBean roleBean) {
        ResponseData responseResult = new ResponseData();
        try {
            saveEntity(roleBean);
            responseResult.setCode(Const.CODE_SUCCESS);
            responseResult.setMsg(Const.CODE_SUCCESS_STR);
        } catch (DuplicateKeyException ex) {
            responseResult.setMsg("该角色编码已存在，请重新输入！");
        }
        return responseResult;
    }

    public boolean existCode(RoleBean roleBean) {
        QueryWrapper<Role> condition = new QueryWrapper<>();
        condition.eq("code", roleBean.getCode());
        Role role = getOne(condition);
        return role != null;
    }



    @Override
    public QueryWrapper<Role> queryCondition(RoleBean bean) {
        QueryWrapper<Role> condition = super.queryCondition(bean);
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), SqlHelper.getSqlLikeParams(bean.getName()));
        }
        if (StringUtils.isNotBlank(bean.getCode())) {
            condition.eq("code", bean.getCode());
        }
        return condition;
    }

    public void deleteRole(Long id) {
        removeById(id);
        // 删除关联关系
        QueryWrapper condition = new QueryWrapper<>();
        condition.eq("role_id", id);
    }

    public List<LabelNode> getLabelNodes() {
        QueryWrapper<Role> condition = super.queryCondition(null);
        List<Role> roleList = list(condition);
        List<LabelNode> labelNodes = new ArrayList<>();
        roleList.forEach(role -> {
            LabelNode labelNode = new LabelNode();
            labelNode.setLabel(role.getName());
            labelNode.setValue(role.getId());
            labelNodes.add(labelNode);
        });
        return labelNodes;
    }
}
