package com.jcloud.dictionary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.jcloud.common.service.LongToStringSerializer;
import com.jcloud.dictionary.support.DictionaryBeanSerializerFactory;
import com.jcloud.redis.component.RedisConfig;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 字典服务，redis 使用的数据库是2
 *
 * @author jiaxm
 * @date 2021/4/12
 */
@Configuration
public class DictionaryConfig implements InitializingBean {

    private static LettuceConnectionFactory lettuceConnectionFactory;

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private ClientResources clientResources;

    /**
     * 字典库
     */
    @Value("${system.dictionary.database:2}")
    private Integer dictionaryDataBase;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Object> dictionaryRedisTemplate() {
        RedisTemplate redisTemplate = RedisConfig.getRedisTemplate(getLettuceConnectionFactory());
        RedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        redisTemplate.setHashKeySerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        return redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 设置objectMapper 支持 字典转换
        DictionaryBeanSerializerFactory serializerFactory = new DictionaryBeanSerializerFactory(null);
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        // 解决js long 精度丢失
        LongToStringSerializer longToStringSerializer = new LongToStringSerializer(Long.class);
        simpleSerializers.addSerializer(Long.class, longToStringSerializer);
        simpleSerializers.addSerializer(Long.TYPE, longToStringSerializer);
        objectMapper.setSerializerFactory(serializerFactory.withAdditionalSerializers(simpleSerializers));
    }

    /**
     * LettuceConnectionFactory 构建
     * @return
     */
    public LettuceConnectionFactory getLettuceConnectionFactory() {
        if (lettuceConnectionFactory == null) {
            synchronized (DictionaryConfig.class) {
                if (lettuceConnectionFactory == null) {
                    lettuceConnectionFactory = redisConnectionFactory(clientResources);
                    lettuceConnectionFactory.afterPropertiesSet();
                }
            }
        }
        return lettuceConnectionFactory;
    }


    private LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) {
        LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(clientResources);
        return new LettuceConnectionFactory(getStandaloneConfig(), clientConfig);
    }

    protected final RedisStandaloneConfiguration getStandaloneConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        config.setUsername(redisProperties.getUsername());
        config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        config.setDatabase(dictionaryDataBase);
        return config;
    }

    private LettuceClientConfiguration getLettuceClientConfiguration(ClientResources clientResources) {
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = createBuilder();
        applyProperties(builder);
        builder.clientOptions(createClientOptions());
        builder.clientResources(clientResources);
        return builder.build();
    }


    private ClientOptions createClientOptions() {
        ClientOptions.Builder builder = initializeClientOptionsBuilder();
        Duration connectTimeout = redisProperties.getConnectTimeout();
        if (connectTimeout != null) {
            builder.socketOptions(SocketOptions.builder().connectTimeout(connectTimeout).build());
        }
        return builder.timeoutOptions(TimeoutOptions.enabled()).build();
    }


    private ClientOptions.Builder initializeClientOptionsBuilder() {
        if (redisProperties.getCluster() != null) {
            ClusterClientOptions.Builder builder = ClusterClientOptions.builder();
            RedisProperties.Lettuce.Cluster.Refresh refreshProperties = redisProperties.getLettuce().getCluster().getRefresh();
            ClusterTopologyRefreshOptions.Builder refreshBuilder = ClusterTopologyRefreshOptions.builder()
                    .dynamicRefreshSources(refreshProperties.isDynamicRefreshSources());
            if (refreshProperties.getPeriod() != null) {
                refreshBuilder.enablePeriodicRefresh(refreshProperties.getPeriod());
            }
            if (refreshProperties.isAdaptive()) {
                refreshBuilder.enableAllAdaptiveRefreshTriggers();
            }
            return builder.topologyRefreshOptions(refreshBuilder.build());
        }
        return ClientOptions.builder();
    }

    private LettuceClientConfiguration.LettuceClientConfigurationBuilder applyProperties(
            LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
        if (redisProperties.isSsl()) {
            builder.useSsl();
        }
        if (redisProperties.getTimeout() != null) {
            builder.commandTimeout(redisProperties.getTimeout());
        }
        if (redisProperties.getLettuce() != null) {
            RedisProperties.Lettuce lettuce = redisProperties.getLettuce();
            if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout());
            }
        }
        if (StringUtils.hasText(redisProperties.getClientName())) {
            builder.clientName(redisProperties.getClientName());
        }
        return builder;
    }

    private LettuceClientConfiguration.LettuceClientConfigurationBuilder createBuilder() {
        return LettucePoolingClientConfiguration.builder().poolConfig(getPoolConfig(redisProperties.getLettuce().getPool()));
    }

    private GenericObjectPoolConfig<?> getPoolConfig(RedisProperties.Pool properties) {
        GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        if (properties.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
        }
        if (properties.getMaxWait() != null) {
            config.setMaxWaitMillis(properties.getMaxWait().toMillis());
        }
        return config;
    }
}
