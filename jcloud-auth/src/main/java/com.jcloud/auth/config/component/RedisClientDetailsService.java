package com.jcloud.auth.config.component;

import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.TypeUtil;
import com.jcloud.remote.admin.ClientDetailsRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author jiaxm
 * @date 2021/3/25
 */
@Service
public class RedisClientDetailsService implements ClientDetailsService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static Logger log = LoggerFactory.getLogger(RedisClientDetailsService.class);

    @Autowired
    private ClientDetailsRemoteService clientDetailsRemoteService;

    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String CACHE_CLIENT_KEY = "client_details";

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ResponseData commonRespon = clientDetailsRemoteService.getClientDetails(clientId);
        if (commonRespon.getCode() == Const.CODE_SUCCESS) {
            LinkedHashMap details = (LinkedHashMap) commonRespon.getData();

            BaseClientDetails clientDetails = new BaseClientDetails(
                    TypeUtil.toStr(details.get("clientId")),
                    TypeUtil.toStr(details.get("resourceIds")),
                    TypeUtil.toStr(details.get("scope")),
                    TypeUtil.toStr(details.get("authorizedGrantTypes")),
                    TypeUtil.toStr(details.get("authorities")),
                    TypeUtil.toStr(details.get("webServerRedirectUri"))
                    );
            clientDetails.setClientSecret(TypeUtil.toStr(details.get("clientSecret")));
            clientDetails.setAccessTokenValiditySeconds(TypeUtil.toInt(details.get("accessTokenValidity")));
            String additionInfo = TypeUtil.toStr(details.get("additionalInformation"));
            if (StringUtils.isNotBlank(additionInfo)) {
                clientDetails.setAdditionalInformation(JsonUtils.readObject(additionInfo, LinkedHashMap.class));
            }
            clientDetails.setRefreshTokenValiditySeconds(TypeUtil.toInt(details.get("refreshTokenValidity")));
            String autoProve = TypeUtil.toStr(details.get("autoapprove"));
            if (StringUtils.isNotBlank(autoProve)) {
                Set<String> scopeList = org.springframework.util.StringUtils.commaDelimitedListToSet(autoProve);
                clientDetails.setAutoApproveScopes(scopeList);
            }
            return clientDetails;
        }
        throw new NoSuchClientException("该client不存在");
    }
}
