package com.jcloud.security.config.component.localsecurity;

import cn.hutool.core.bean.BeanUtil;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.remote.admin.UserRemoteService;
import com.jcloud.security.bean.ShiroUser;
import com.jcloud.security.consts.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * 遠程獲取用戶
 */
public class RemoteUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRemoteService userRemoteService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseData commonRespon = userRemoteService.loadUserByName(username);
        return transResponse(commonRespon);
    }

    /**
     * 微信 unionId 登录
     * @param wxUserInfo
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUnionId(Map<String, Object> wxUserInfo) throws UsernameNotFoundException {
        ResponseData commonRespon = userRemoteService.loadUserByUnionId(wxUserInfo);
        return transResponse(commonRespon);
    }

    /**
     * 手机号登录
     * @param phone
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        ResponseData commonRespon = userRemoteService.loadUserByPhone(phone);
        return transResponse(commonRespon);
    }

    protected UserDetails transResponse(ResponseData commonRespon) {
        if (commonRespon.getCode() == Const.CODE_SUCCESS) {
            ShiroUser shiroUser = new ShiroUser();
            BeanUtil.fillBeanWithMap((Map<?, ?>) commonRespon.getData(), shiroUser, false);
            return shiroUser;
        }
        throw new BadCredentialsException(SecurityConstants.BAD_CREDENTIALS);
    }
}
