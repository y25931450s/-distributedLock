package com.xiaoying.base.lock.init;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.xiaoying.base.lock.exception.DistributedLockErrorMessage;
import com.xiaoying.base.lock.exception.DistributedLockServiceException;

/**
 * @author gaofang
 * 获取apollo配置
 * @date 2019/8/30 16:23
 */
public class DataSourceManager {


    public static ApolloData registerRules() {
        Config config = ConfigService.getConfig("server.lock_common");
        ApolloData apolloData = new ApolloData();
        apolloData.setZkAddress(config.getProperty("com.quwei.xiaoying.lock.zookeeper.url", ""));
        apolloData.setSessionTimeout(config.getIntProperty("com.quwei.xiaoying.lock.zk.session.timeout.ms", 10000));
        apolloData.setConnectionTimeout(config.getIntProperty("com.quwei.xiaoying.lock.zk.connection.timeout.ms", 10000));
        apolloData.setRedisUrl(config.getProperty("com.quwei.xiaoying.lock.redis.url", ""));
        apolloData.setRedisPassword(config.getProperty("com.quwei.xiaoying.lock.redis.password", ""));
        return apolloData;
    }

}
