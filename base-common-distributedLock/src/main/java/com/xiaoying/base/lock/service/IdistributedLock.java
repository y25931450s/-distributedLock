package com.xiaoying.base.lock.service;

import java.util.concurrent.TimeUnit;

public interface IdistributedLock extends AutoCloseable {

    /**
     * 阻塞的获取锁，如果获取到锁，从该方法返回
     */
    void lock();


    /**
     * 尝试非阻塞的获取锁，调用方法之后立马进行返回，如果能获取返回true,不能则返回false
     *
     * @return
     */
    boolean tryLock();


    /**
     * 可超时的获取锁，如果能获取返回true,不能则返回false
     *
     * @param waitTime
     * @param unit
     * @return
     */
    boolean tryLock(Long waitTime, TimeUnit unit);


    /**
     * 释放锁
     */
    void unlock();


}
