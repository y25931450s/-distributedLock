package com.xiaoying.base.lock;

import com.xiaoying.base.lock.annotation.DistributedLock;
import com.xiaoying.base.lock.annotation.EnableLock;
import com.xiaoying.base.lock.annotation.LockKey;
import com.xiaoying.base.lock.enums.LockType;
import com.xiaoying.base.lock.service.Dlock;
import com.xiaoying.base.lock.service.IdistributedLock;
import lombok.Data;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@EnableLock
public class RedisLockTest {


    @Autowired
    RedisLockService redisLockService;

    private static final int THREAD_COUNT = 1;

    private int count = 1;

    @Test
    public void lockTest() {
        IdistributedLock lock = new Dlock("RedisLockTest223", LockType.redis);
        lock.lock();
        sleep(500);
        System.out.println("阻塞锁获取成功！");
        lock.unlock();
    }

    /**
     * 测试自动释放锁资源
     */
    @Test
    public void lockAutoTest() {

        try (IdistributedLock lock = new Dlock("RedisLockAutoTest", LockType.redis)) {
            lock.lock();
            System.out.println("获取锁成功，自动释放");
        } catch (Exception e) {
        }
    }

    @Test
    public void tryLockTest() {
        IdistributedLock lock = new Dlock("RedisTryLockTest", LockType.redis);
        if (lock.tryLock()) {
            sleep(500);
            System.out.println("非阻塞立即返回锁获取锁成功");
            lock.unlock();
        }
    }

    @Test
    public void tryLockParamTest() {
        IdistributedLock lock = new Dlock("RedisTryLockParamTest", LockType.redis);
        lock.tryLock(3000L, TimeUnit.MILLISECONDS);
        sleep(100);
        System.out.println("可超时获取锁成功！");
        lock.unlock();
    }


    /**
     * redis可重入测试
     */
    @Test
    public void ReentrantLock() {
        Dlock dlock = new Dlock("RedisReentrantLockTest", LockType.redis);
        dlock.lock();
        System.out.println("redis获取锁成功");
        dlock.lock();
        System.out.println("redis重入获取锁成功");
        dlock.unlock();
        dlock.unlock();
    }

    /**
     * redis并发测试
     */
//    @Test
    public void redistestThreadJunit() {
        try {
            // Runner数组，相当于并发多少个
            TestRunnable[] trs = new TestRunnable[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                trs[i] = new TestRunnable() {
                    @Override
                    public void runTest() throws Throwable {
//                        getRedisLock();
//                        IdistributedLock lock = new Dlock("RedisLockTest223", LockType.redis);
//                        lock.unlock();
//                        System.out.println(Thread.currentThread().getName());
                    }
                };
            }
            // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
            MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
            // 并发执行数组里定义的内容
            mttr.runTestRunnables();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //避免程序提前结束
        while (true) ;

    }

    public void getRedisLock() {
        IdistributedLock lock = new Dlock("redis", LockType.redis);
        IdistributedLock lockb = new Dlock("redis2", LockType.redis);
        lockb.lock();
        int count = this.count++;
        lockb.unlock();

        System.out.println("第" + count + "等待锁");
        if (lock.tryLock(30000L, TimeUnit.MILLISECONDS)) {
            Thread th = Thread.currentThread();
            System.out.println("Tread name:" + th.getName());
            System.out.println("第" + count + "  获取锁成功");

        }
        sleep(2000);
        lock.unlock();
    }


    /**
     * reidis自动延长任务
     */


    @Test
    public void lockTimeExtend() {
        Dlock dlock = new Dlock("123", LockType.redis);
        if (dlock.tryLock()) {
//            while (true) ;
            sleep(3000);
            dlock.unlock();
        }
    }


    /**
     * reidis注解测试
     */


    @Test
    public void ReidsAnnotationTest() {
        Lock lock = new Lock();
        lock.setAuid(1L);
        redisLockService.lock(lock);
    }

    @Data
    public class Lock {
        @LockKey
        public Long auid;
    }

    @Configuration
    public static class RedisLockService {
        @DistributedLock(key = "yushuo", lockType = LockType.redis)
        public void lock(Lock lock) {
            System.out.println(lock.auid.toString());
        }
    }


    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




