package com.jcloud.admin.service;

import com.jcloud.admin.bean.UserBean;
import com.jcloud.admin.entity.User;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.service.CrudListService;
import com.jcloud.security.bean.ShiroUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends CrudListService<User, UserBean>, UserDetailsService {

    public ResponseData saveUser(UserBean bean);

    public String resetPassword(List<Long> idList);

    public UserBean findByUuid(String uuid);

    /**
     * 头像修改
     * @param avatar
     * @return
     */
    public ResponseData changeAvatar(String avatar);

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public ResponseData editPassword(String oldPassword, String newPassword);

    /**
     * 手机号重复校验
     * true 是不存在，false存在
     * @param phone
     * @param id
     * @return
     */
    public boolean checkByPhone(String phone, Long id);


    /**
     * 刷新用户session数据
     * @return
     */
    public ResponseData refreshUser();

    /**
     * 根据unionid 获取用户信息
     * @param info
     * @return
     */
    public ResponseData loadUserByUnionId(Map<String, Object> info);

    /**
     * 根据手机号获取用户信息
     * @param phone
     * @return
     */
    public ResponseData loadUserByPhone(String phone);

    /**
     * 简单注册，用于数据同步，数据同步需要传送 手机号，用户姓名，这样中台进行注册
     * @param userName
     * @param phone
     * @return 用户id
     */
    public ResponseData<Long> simpleRegUser(String userName, String phone, Long sourceFrom);


    /**
     * 获取用户信息
     * @param ids
     * @return
     */
    public ResponseData<List<ShiroUser>> getUserByIds(List<Long> ids);

    /**
     * 获取用户id
     * @param phone
     * @return
     */
    public Long getUserIdByPhone(String phone);
}
