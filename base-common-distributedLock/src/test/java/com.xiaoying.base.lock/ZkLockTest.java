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

import java.util.concurrent.*;


@RunWith(SpringRunner.class)
@EnableLock
public class ZkLockTest {

    //并发线程数
    private static final int THREAD_COUNT = 10;

    @Test
    public void lockTest() {
        IdistributedLock lock = new Dlock("ZKLockTest", LockType.zk);
        lock.lock();
        sleep(500);
        Thread th = Thread.currentThread();
        System.out.println("Tread name:" + th.getName());
        System.out.println("阻塞锁获取成功！");
        lock.unlock();
    }

    @Test
    public void tryLockTest() {
        IdistributedLock lock = new Dlock("ZKTryLockTest", LockType.zk);
        if (lock.tryLock()) {
            sleep(500);
            System.out.println("非阻塞立即返回锁获取锁成功");
            lock.unlock();
        }
    }

    @Test
    public void tryLockParamTest() {
        IdistributedLock lock = new Dlock("ZKTryLockParamTest", LockType.zk);
        lock.tryLock(3000L,TimeUnit.MILLISECONDS);
        sleep(100);
        System.out.println("可超时获取锁成功！");
        lock.unlock();
    }

    @Test
    public void reentrantTest() {
        IdistributedLock lock = new Dlock("ZKReentrantTest", LockType.zk);
        lock.lock();
        System.out.println("获取锁成功！");
        lock.lock();
        System.out.println("可重入获取锁成功！");
        lock.unlock();
        lock.unlock();
    }

    /**
     * 测试自动释放锁资源
     */
    @Test
    public void lockAutoTest() {

        try (IdistributedLock lock = new Dlock("ZkLockAutoTest", LockType.zk)) {
            lock.lock();
            System.out.println("获取锁成功，自动释放");
        } catch (Exception e) {
        }
    }

    /**
     * 并发测试，只需将要并发测试的上述方法写入即可
     */

//    @Test
    public void ZKTestThreadJunit() {
        try {
            // Runner数组，相当于并发多少个
            TestRunnable[] trs = new TestRunnable[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                trs[i] = new TestRunnable() {
                    @Override
                    public void runTest() throws Throwable {
                        // 并发测试方法
                        lockTest();
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
    }




    /**
     * zk注解测试
     */

    @Autowired
    private ZkLockService zkLockService;


    @Test
    public void ZkLockTest() {
        Lock lock = new Lock();
        lock.setAuid(8L);
        zkLockService.lock(lock);
    }

    @Configuration
    public static class ZkLockService {
        @DistributedLock(key = "gf", lockType = LockType.zk)
        public void lock(Lock lock) {
            System.out.println("注解测试成功 ---   auid =" + lock.auid.toString());
        }
    }


    @Data
    public class Lock {

        @LockKey
        public Long auid;
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
