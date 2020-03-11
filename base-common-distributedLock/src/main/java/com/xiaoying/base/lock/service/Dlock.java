package com.xiaoying.base.lock.service;


import com.xiaoying.base.lock.enums.LockType;
import com.xiaoying.base.lock.factory.LockFactory;
import com.xiaoying.base.lock.init.CatTransaction;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author
 */
public class Dlock implements IdistributedLock {


    private IdistributedLock lock;

    private static String TRACE_PREFIX = "DistributedLock.";

    private static String SUCCESS = "success";

    private static String FAILED = "failed";

    private static String OP_LOCK_TRACE = TRACE_PREFIX + "Lock";

    private static String LOCK_STATE = "Lock.Acquire.State";

    private static String UNLOCK_STATE = "Unlock.Release.State";

    private static String OP_UNLOCK_TRACE = TRACE_PREFIX + "Unlock";

    private String key;

    public Dlock(String key, LockType lockType) {

        //判断锁类型
        if (Objects.isNull(lockType)) {
            lockType = LockType.redis;
        }
        //工厂类生成锁
        lock = LockFactory.createLock(lockType, key);
        this.key = key;
    }


    @Override
    public void lock() {

        CatTransaction catTransaction = new CatTransaction(OP_LOCK_TRACE, key);
        try {
            lock.lock();
            catTransaction.addEvent(OP_LOCK_TRACE, SUCCESS);
        } catch (Throwable ex) {
            catTransaction.addEvent(OP_LOCK_TRACE, FAILED);
            catTransaction.setStatus(ex);
            throw ex;
        } finally {
            catTransaction.complete();
        }
    }

    @Override
    public boolean tryLock() {
        CatTransaction catTransaction = new CatTransaction(OP_LOCK_TRACE, key);
        try {
            boolean acquired = lock.tryLock();
            catTransaction.addEvent(LOCK_STATE, acquired ? SUCCESS : FAILED);
            return acquired;
        } finally {
            catTransaction.complete();
        }

    }

    @Override
    public boolean tryLock(Long waitTime, TimeUnit unit) {
        CatTransaction catTransaction = new CatTransaction(OP_LOCK_TRACE, key);
        try {
            boolean acquired = lock.tryLock(waitTime, unit);
            catTransaction.addEvent(LOCK_STATE, acquired ? SUCCESS : FAILED);
            return acquired;
        }finally {
            catTransaction.complete();

        }
    }

    @Override
    public void unlock() {
        CatTransaction catTransaction = new CatTransaction(OP_UNLOCK_TRACE, key);
        try {
            lock.unlock();
            catTransaction.addEvent(UNLOCK_STATE, SUCCESS);
        } catch (Throwable ex) {
            catTransaction.addEvent(UNLOCK_STATE, FAILED);
            catTransaction.setStatus(ex);
            throw ex;
        }finally {
            catTransaction.complete();
        }
    }

    @Override
    public void close() {
        this.unlock();
    }
}
