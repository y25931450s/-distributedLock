package com.xiaoying.base.lock.service.redis;

import com.xiaoying.base.lock.exception.DistributedLockErrorMessage;
import com.xiaoying.base.lock.exception.DistributedLockServiceException;
import com.xiaoying.base.lock.service.IdistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLock implements IdistributedLock {
    private String key;
    private RLock lock;

    public RedisLock(RedissonClient redissonClient, String key) {
        this.lock = redissonClient.getLock(key);
        this.key = key;
    }

    @Override
    public void lock() {

        try {
            lock.lock();
        } catch (Exception e) {

            log.error("base-common-distributedLock] error: ", e);
            throw new DistributedLockServiceException(DistributedLockErrorMessage.REDIS_LOCK_FAIL);
        }
    }

    @Override
    public boolean tryLock(Long waitTime, TimeUnit unit) {
        boolean acquire = false;
        try {
            acquire = lock.tryLock(waitTime, unit);
        } catch (InterruptedException e) {
            log.error("base-common-distributedLock] error: ", e);
        }
        return acquire;
    }

    @Override
    public boolean tryLock() {
        boolean acquire = false;
        try {
            acquire = lock.tryLock();
        } catch (Exception e) {
            log.error("base-common-distributedLock] error: ", e);
        }
        return acquire;

    }

    @Override
    public void unlock() {
        try {
            if (!lock.isHeldByCurrentThread()) {
                return;
            }
            lock.unlock();
        } catch (Exception e) {
            log.error("base-common-distributedLock] error:", e);
            throw new DistributedLockServiceException(DistributedLockErrorMessage.REDIS_UNLOCK_FAIL);
        }
    }


    /**
     * 自动释放资源
     */
    @Override
    public void close() {
        unlock();
    }
}
