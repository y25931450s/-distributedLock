package com.xiaoying.base.lock.annotation;


import com.xiaoying.base.lock.enums.LockType;


import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DistributedLock {
    /**
     * 锁命名
     * @return
     */
    String key() default "";

    /**
     * 锁类型 redis、zk
     * @return
     */
    LockType lockType() default LockType.redis;

}
