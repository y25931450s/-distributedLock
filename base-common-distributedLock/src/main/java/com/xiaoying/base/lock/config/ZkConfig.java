package com.xiaoying.base.lock.config;

import com.xiaoying.base.lock.init.ApolloData;
import com.xiaoying.base.lock.init.DataSourceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author gaofang
 */
@Slf4j
public class ZkConfig {

    private volatile static CuratorFramework client;

    static {
        getZkClient();
    }


    public static CuratorFramework getZkClient() {
        if (client == null) {
            synchronized (ZkConfig.class) {
                if (client == null) {
                    ApolloData apolloData = DataSourceManager.registerRules();
                    client = CuratorFrameworkFactory.builder().connectString(apolloData.getZkAddress())
                            .sessionTimeoutMs(apolloData.getSessionTimeout())
                            .connectionTimeoutMs(apolloData.getConnectionTimeout())
                            .namespace("lock")
                            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                            .build();
                    client.start();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> client.close()));
                }
            }
        }

        return client;
    }

}
