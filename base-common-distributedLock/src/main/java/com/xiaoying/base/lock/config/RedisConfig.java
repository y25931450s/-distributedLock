package com.xiaoying.base.lock.config;

import com.xiaoying.base.lock.init.ApolloData;
import com.xiaoying.base.lock.init.DataSourceManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;


public class RedisConfig {



    private volatile static RedissonClient redissonClient;

    static {
        getRedisClient();
    }

    public static RedissonClient getRedisClient() {
        if (redissonClient == null) {
            synchronized (RedisConfig.class) {
                if (redissonClient == null) {
                    ApolloData apolloData = DataSourceManager.registerRules();
                    Config config = new Config();
                    config.useSingleServer().setAddress(apolloData.getRedisUrl());
                    config.useSingleServer().setPassword(apolloData.getRedisPassword());
                    redissonClient = Redisson.create(config);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> redissonClient.shutdown()));
                }
            }
        }

        return redissonClient;
    }

}
