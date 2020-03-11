package com.xiaoying.base.lock.factory;

import com.xiaoying.base.lock.config.RedisConfig;
import com.xiaoying.base.lock.config.ZkConfig;
import com.xiaoying.base.lock.enums.LockType;
import com.xiaoying.base.lock.exception.DistributedLockErrorMessage;
import com.xiaoying.base.lock.exception.DistributedLockServiceException;
import com.xiaoying.base.lock.service.IdistributedLock;
import com.xiaoying.base.lock.service.redis.RedisLock;
import com.xiaoying.base.lock.service.zk.ZkLock;
import com.xiaoying.base.lock.util.KeyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author yushuo
 * @version 1.0
 * @date 2019-09-09 11:03
 */
public abstract class LockFactory {

    private static Map<LockType, Function<String, IdistributedLock>> lockFactoryMap = new HashMap<>(2);

    static {
        // redis
        lockFactoryMap.put(LockType.redis, key -> new RedisLock(RedisConfig.getRedisClient(), KeyUtils.getClassKey(key)));
        // zk
        lockFactoryMap.put(LockType.zk, key -> new ZkLock("/" + KeyUtils.getClassKey(key), ZkConfig.getZkClient()));

    }

    private static Function<String, IdistributedLock> NOT_SUPPORT = s -> {
        throw new DistributedLockServiceException(DistributedLockErrorMessage.LOCK_TYPE_NOT_LOCK);
    };


    public static IdistributedLock createLock (LockType lockType, String key) {
        return lockFactoryMap.getOrDefault(lockType, NOT_SUPPORT).apply(key);
    }

}
