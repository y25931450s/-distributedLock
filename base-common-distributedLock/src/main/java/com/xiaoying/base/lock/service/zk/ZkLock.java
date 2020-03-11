package com.xiaoying.base.lock.service.zk;

import com.xiaoying.base.lock.exception.DistributedLockErrorMessage;
import com.xiaoying.base.lock.exception.DistributedLockServiceException;
import com.xiaoying.base.lock.service.IdistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author gaofang
 * @date 2019/8/27 12:17
 */
@Slf4j
public class ZkLock implements IdistributedLock {


    private InterProcessMutex interProcessMutex = null;
    private CuratorFramework client = null;
    private String key;

    public ZkLock(String key, CuratorFramework client) {
        this.key = key;
        this.client = client;
        this.interProcessMutex = new InterProcessMutex(client, this.key);

    }

    @Override
    public void lock() {
        try {
            interProcessMutex.acquire();
        } catch (Exception e) {
            log.error("base-common-distributedLock] error: ", e);
            throw new DistributedLockServiceException(DistributedLockErrorMessage.ZK_LOCK_FAIL);
        }
    }

    @Override
    public boolean tryLock(Long time, TimeUnit timeUnit) {
        boolean acquire = false;
        try {
            acquire = interProcessMutex.acquire(time, timeUnit);
        } catch (Exception e) {
            log.error("base-common-distributedLock] error: ", e);
        }
        return acquire;
    }

    @Override
    public boolean tryLock() {
        boolean acquire = false;
        try {
            acquire = interProcessMutex.acquire(0L, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("base-common-distributedLock] error: ", e);
        }
        return acquire;
    }

    @Override
    public void unlock() {
        try {
            if (!interProcessMutex.isAcquiredInThisProcess()) {
                return;
            }
            interProcessMutex.release();
            if (CollectionUtils.isEmpty(client.getChildren().forPath(key))) {
                client.delete().forPath(key);
            }
        } catch (Exception e) {
            log.error("base-common-distributedLock] error: ", e);
            throw new DistributedLockServiceException(DistributedLockErrorMessage.ZK_UNLOCK_FAIL);
        }

    }

    @Override
    public void close() {
        unlock();
    }
}
