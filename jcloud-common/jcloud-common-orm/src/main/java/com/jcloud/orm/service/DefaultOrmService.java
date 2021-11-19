package com.jcloud.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.service.Transformer;
import com.jcloud.common.util.CamelUtils;
import com.jcloud.common.util.ReflectUtil;
import com.jcloud.common.util.UUIDUtils;
import com.jcloud.orm.domain.DataBasePage;
import com.jcloud.orm.model.BaseModel;
import com.jcloud.security.bean.ShiroUser;
import com.jcloud.security.utils.PermissionUtil;
import com.jcloud.security.utils.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * crud 列表通用
 * 1、建立相关实体 entity && 输出输入 bean
 * 2、可以覆盖 钩子函数 {@see #beforeSave #afterSave}，自定义对实体save前后操作,getIgnoreProperties,可以自定义输入忽略字段
 * 3、基础列表查询只需要覆盖 queryCondition 方法，调用 {@code pageList(CustomPage page, B bean)} 默认排序 {@code defaultOrderBy}
 *
 * @param <M> mapper
 * @param <T> 实体
 * @param <B> 输出值，传入参数bean
 */
public abstract class DefaultOrmService<M extends BaseMapper<T>, T extends BaseModel, B> extends ServiceImpl<M, T> implements Transformer<T, B>, CrudListService<T, B> {

    /**
     * 实体公共信息赋值
     * @param t
     */
    public void insertCommonInfo(BaseModel t) {
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

    /**
     * 获取mybatis-plus page
     * @param page
     * @return
     */
    public Page getPage(DataBasePage page) {
        Page iPage = new Page<>();
        iPage.setSize(page.getPageSize());
        iPage.setCurrent(page.getCurrentPage());
        return iPage;
    }

    /**
     * 新增修改的时候忽略字段
     * @return
     */
    protected List<String> getIgnoreProperties() {
        List<String> IGNORE_FIELDS = new ArrayList<>();
        IGNORE_FIELDS.add("createTime");
        IGNORE_FIELDS.add("uuid");
        return IGNORE_FIELDS;
    }

    /**
     * 保存或者更新实体
     * @param bean
     * @return
     */
    public T saveEntity(B bean) {
        T entity = null;
        Object id = ReflectUtil.getFieldValue("id", bean.getClass(), bean);
        if (id == null) { // 新增
            entity = insertEntity(bean);
        } else { // 编辑
            entity = editEntity(bean, (Long) id);
        }
        return entity;
    }

    /**
     * 新增实体
     * @param bean
     * @return
     */
    public T insertEntity(B bean) {
        Class<T> clazz = currentModelClass();
        T entity = (T) BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(bean, entity, getIgnoreProperties().toArray(new String[]{}));
        insertCommonInfo(entity);
        beforeSave(entity, bean);
        baseMapper.insert(entity);
        afterSave(entity, bean);
        return entity;
    }

    /**
     * 编辑实体
     * @param bean
     * @param id
     * @return
     */
    public T editEntity(B bean, Long id) {
        T entity = baseMapper.selectById(id);
        if (entity != null) {
            BeanUtils.copyProperties(bean, entity, getIgnoreProperties().toArray(new String[]{}));
            insertCommonInfo(entity);
            beforeSave(entity, bean);
            baseMapper.updateById(entity);
            afterSave(entity, bean);
        }
        return entity;
    }

    /**
     * before save the entity hook
     *
     * @param entity
     */
    protected void beforeSave(T entity, B bean) {

    }

    /**
     * after save the entity hook
     *
     * @param entity
     */
    protected void afterSave(T entity, B bean) {

    }

    /**
     * 根据QueryWrapper 获取列表
     * @param page
     * @param condition
     * @return
     */
    public ResponseData pageList(DataBasePage page, QueryWrapper<T> condition) {
        return pageList(page, condition, this);
    }

    /**
     * default pageList
     *  根据前端传入的参数进行列表获取
     * @param page
     * @param bean
     * @return
     */
    public ResponseData pageList(DataBasePage page, B bean) {
        QueryWrapper<T> condition = queryCondition(bean);
        return pageList(page, condition);
    }

    public ResponseData pageList(DataBasePage page, QueryWrapper<T> condition, Transformer<T, B> transformer) {
        Page<T> iPage = getPage(page);
        Page<T> pageList = page(iPage, condition);
        List<T> dataList = pageList.getRecords();
        List<B> beanList = transformer.convert(dataList);
        DataBasePage commonPage = new DataBasePage(pageList, beanList);
        ResponseData result = ResponseData.getSuccessInstance();
        result.setData(commonPage);
        return result;
    }

    /**
     * 为支持前端排序，定义 前端传入 orderField，orderWay 公共字段
     * @param bean
     * @param condition
     */
    private void defaultOrderBy(B bean, QueryWrapper<T> condition) {
        if (bean == null) {
            return;
        }
        Object orderField = ReflectUtil.getFieldValue("orderField", bean.getClass(), bean);
        Object orderWay = ReflectUtil.getFieldValue("orderWay", bean.getClass(), bean);
        if (orderField != null && !StringUtils.isEmpty(orderField.toString()) && !StringUtils.isEmpty(orderWay)) {
            String entityColumn = CamelUtils.dealCamelCase(orderField.toString());
            if (orderWay.toString().equals(Const.ORDER_ASC)) {
                condition.orderByAsc(entityColumn);
            } else {
                condition.orderByDesc(entityColumn);
            }
        } else {
            defaultOrderBy(condition);
        }
    }

    /**
     * 默认的排序 id倒序
     * @param condition
     */
    protected void defaultOrderBy(QueryWrapper<T> condition) {
        condition.orderByDesc("id");
    }

    /**
     * 列表查询条件，真正使用需覆盖
     *
     * @param bean
     * @return
     */
    public QueryWrapper<T> queryCondition(B bean) {
        QueryWrapper<T> condition = new QueryWrapper();
        condition.eq("deleted", 0);
        defaultOrderBy(bean, condition);
        return condition;
    }

    /**
     * 加表格默认排序
     * @param bean
     * @return
     */
    public QueryWrapper<T> defaultOrderQueryCondition(B bean) {
        QueryWrapper<T> condition = queryCondition(bean);
        defaultOrderBy(bean, condition);
        return condition;
    }

    /**
     * 添加默认权限departmentId
     * @param condition
     */
    protected void addDataScopeCondition(QueryWrapper<T> condition) {
        ShiroUser user = SecurityUtil.getCurrentUser();
        // 如果不是超级管理员的加上数据权限
        if (PermissionUtil.hasSuperAdmin(user)) {
            condition.eq("department_id", user.getDepartmentId());
        }
    }

    /**
     * 逻辑删除
     * @param idList
     */
    public void logicDelete(List<Long> idList) {
        idList.forEach(id -> {
            logicDelete(id);
        });
    }

    /**
     * 逻辑删除
     * @param id
     */
    public void logicDelete(Long id) {
        T entity = getById(id);
        if (entity != null) {
            entity.setDeleted(1);
            insertCommonInfo(entity);
            updateById(entity);
        }
    }

    /**
     * 物理删除
     * @param idList
     */
    public void physicsDelete(List<Long> idList) {
        idList.forEach(id -> {
            physicsDelete(id);
        });
    }

    /**
     * 物理删除
     * @param id
     */
    public void physicsDelete(Long id) {
        baseMapper.deleteById(id);
    }

}
