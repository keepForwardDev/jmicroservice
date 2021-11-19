package com.jcloud.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.consts.PrivilegesType;
import com.jcloud.admin.entity.ClientDetails;
import com.jcloud.admin.entity.User;
import com.jcloud.admin.mapper.ClientDetailsMapper;
import com.jcloud.admin.mapper.ClientRoleMapper;
import com.jcloud.admin.mapper.UserMapper;
import com.jcloud.admin.mapper.UserRoleMapper;
import com.jcloud.admin.service.PrivilegesCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author jiaxm
 * @date 2021/11/3
 */
@Service
public class PrivilegesCacheServiceImpl implements PrivilegesCacheService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ClientDetailsMapper clientDetailsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ClientRoleMapper clientRoleMapper;

    @Override
    public boolean cachePrivilegesIfAbsent(String key, PrivilegesSaveBean privilegesSaveBean) {
        String privilegeKey = PRIVILEGE_CACHE_KEY + key;
        boolean putSuccess = redisTemplate.opsForValue().setIfAbsent(privilegeKey, privilegesSaveBean, Duration.ofHours(6));
        if (putSuccess) { // 重新刷新api次数缓存
            String apiLimitKeyPattern = API_LIMIT_CACHE_KEY + key + StringPool.ASTERISK;
            Set<String> keySet = redisTemplate.keys(apiLimitKeyPattern);
            if (!CollectionUtil.isEmpty(keySet)) {
                redisTemplate.delete(keySet);
            }
        }
        return putSuccess;
    }

    @Override
    public boolean clearPrivileges(Long id, Integer privilegeType) {
        switch (privilegeType) {
            case PrivilegesType.USER:
                return clearUserCache(id);
            case PrivilegesType.ROLE:
                return clearRoleCache(id);
            case PrivilegesType.CLIENT:
                return clearClientCache(id);
            default:
                break;
        }
        return false;
    }

    private boolean deleteKeysPattern(String pattern) {
        Collection<String> apiKeys = redisTemplate.keys(pattern);
        if (CollectionUtil.isNotEmpty(apiKeys)) {
            redisTemplate.delete(apiKeys);
            return true;
        }
        return false;
    }

    /**
     * 清理用户缓存权限
     * @param id
     * @return
     */
    private boolean clearUserCache(Long id) {
        User user = userMapper.selectById(id);
        if (user != null && StringUtils.isNotBlank(user.getPhone())) {
            String pattern = StringPool.ASTERISK + PrivilegesType.USER + StringPool.COLON + user.getPhone() + StringPool.COLON + StringPool.ASTERISK;
            return deleteKeysPattern(pattern);
        }
        return false;
    }

    /**
     * 清理角色缓存权限
     * @param id
     * @return
     */
    public boolean clearRoleCache(Long id) {
        List<Long> userIds = userRoleMapper.getUserIdsByRoleId(id);
        List<Long> clientIds = clientRoleMapper.getClientIdByRoleId(id);
        if (CollectionUtil.isNotEmpty(userIds)) {
            for (Long userId : userIds) {
                clearUserCache(id);
            }
        }
        if (CollectionUtil.isNotEmpty(clientIds)) {
            for (Long clientId : clientIds) {
                clearClientCache(clientId);
            }
        }
        return true;
    }

    /**
     * 清理客户端权限
     * @param id
     * @return
     */
    public boolean clearClientCache(Long id) {
        ClientDetails client = clientDetailsMapper.selectById(id);
        if (client != null) {
            String pattern = StringPool.ASTERISK + PrivilegesType.CLIENT + StringPool.COLON + client.getClientId() + StringPool.COLON + StringPool.ASTERISK;
            return deleteKeysPattern(pattern);
        }
        return false;
    }


}
