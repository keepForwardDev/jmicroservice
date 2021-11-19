package com.jcloud.remote.admin;

import com.jcloud.common.consts.ServiceConst;
import com.jcloud.common.domain.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户信息服务
 * @author jiaxm
 * @date 2021/4/21
 */
@FeignClient(value = ServiceConst.JCLOUD_ADMIN, contextId = "loginUserRemoteService")
public interface UserRemoteService {


    /**
     * 远程获取用户信息
     * @param userName
     * @return
     */
    @GetMapping(value = "/user/loadUserByUsername")
    ResponseData loadUserByName(@RequestParam(value = "userName") String userName);

    /**
     * 远程用unionId获取用户信息
     * @param info 微信用户信息，里面包含unionId
     * @return
     */
    @GetMapping(value = "/user/loadUserByUnionId")
    ResponseData loadUserByUnionId(@SpringQueryMap Map<String, Object> info);

    /**
     * 远程根据phone获取用户信息
     * @param phone
     * @return
     */
    @GetMapping(value = "/user/loadUserByPhone")
    ResponseData loadUserByPhone(@RequestParam("phone") String phone);

    /**
     *
     * @param userName 用户名称
     * @param phone 手机号
     * @param sourceFrom 用户来源
     * @return 用户id
     */
    @GetMapping(value = "/user/simpleRegUser")
    public ResponseData<Long> simpleRegUser(@RequestParam(value = "userName") String userName, @RequestParam(value = "phone") String phone, @RequestParam(value = "sourceFrom") Long sourceFrom);


    /**
     * 根据id获取用户信息
     * @param ids
     * @return
     */
    @PostMapping(value = "/user/getByIds")
    public ResponseData getByIds(@RequestBody List<Long> ids);
}
