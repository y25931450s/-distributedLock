package com.xiaoying.base.lock.annotation;

import com.xiaoying.base.lock.annotation.autoconfig.LockConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * spring boot开启分布式锁
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LockConfigRegistrar.class)
@Documented
public @interface EnableLock {
}
