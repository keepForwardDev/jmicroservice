package com.jcloud.sentinel.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 规则从nacos 读取，添加监听
 * @author jiaxm
 * @date 2021/4/19
 */
@Slf4j
public class NacosDataSource<T> extends AbstractDataSource<String, T> {

    private static final int DEFAULT_TIMEOUT = 3000;

    private final ExecutorService pool;
    private final Listener configListener;
    private final String groupId;
    private final String dataId;
    private ConfigService configService;

    public NacosDataSource(NacosConfigManager nacosConfigManager, String groupId, String dataId, Converter<String, T> parser) {
        super(parser);
        this.configService = nacosConfigManager.getConfigService();
        this.groupId = groupId;
        this.dataId = dataId;
        this.pool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(1), new NamedThreadFactory("sentinel-nacos-ds-update"), new ThreadPoolExecutor.DiscardOldestPolicy());
        this.configListener = new Listener() {
            public Executor getExecutor() {
                return NacosDataSource.this.pool;
            }

            public void receiveConfigInfo(String configInfo) {
                if (!StringUtil.isBlank(configInfo)) {
                    log.info(String.format("[NacosDataSource] New property value received (dataId: %s, groupId: %s): %s", dataId, groupId, configInfo), new Object[0]);
                    T newValue = NacosDataSource.this.parser.convert(configInfo);
                    NacosDataSource.this.getProperty().updateValue(newValue);
                }
            }
        };
        this.initNacosListener();
        this.loadInitialConfig();
    }

    private void loadInitialConfig() {
        try {
            T newValue = this.loadConfig();
            if (newValue == null) {
                log.warn("[NacosDataSource] WARN: initial config is null, you may have to check your data source", new Object[0]);
            }

            this.getProperty().updateValue(newValue);
        } catch (Exception var2) {
            log.warn("[NacosDataSource] Error when loading initial config", var2);
        }

    }

    private void initNacosListener() {
        try {
            this.configService.addListener(this.dataId, this.groupId, this.configListener);
        } catch (Exception var2) {
            log.warn("[NacosDataSource] Error occurred when initializing Nacos data source", var2);
            var2.printStackTrace();
        }

    }

    public String readSource() throws Exception {
        if (this.configService == null) {
            throw new IllegalStateException("Nacos config service has not been initialized or error occurred");
        } else {
            return this.configService.getConfig(this.dataId, this.groupId, DEFAULT_TIMEOUT);
        }
    }

    public void close() {
        if (this.configService != null) {
            this.configService.removeListener(this.dataId, this.groupId, this.configListener);
        }

        this.pool.shutdownNow();
    }

}
