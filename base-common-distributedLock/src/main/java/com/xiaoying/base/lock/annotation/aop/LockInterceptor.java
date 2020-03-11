package com.xiaoying.base.lock.annotation.aop;

import com.xiaoying.base.lock.annotation.DistributedLock;
import com.xiaoying.base.lock.annotation.LockKey;
import com.xiaoying.base.lock.enums.LockType;
import com.xiaoying.base.lock.service.Dlock;
import com.xiaoying.base.lock.service.IdistributedLock;
import com.xiaoying.base.lock.util.KeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author yushuo
 * @version 1.0
 * @date 2019-09-09 11:03
 */

@Slf4j
public class LockInterceptor implements MethodInterceptor, Serializable {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();

        if (method == null) {
            return invocation.proceed();
        }
        Annotation[][] annotations = method.getParameterAnnotations();
        Class<?>[] getParameterTypes = method.getParameterTypes();
        Object[] getArguments = invocation.getArguments();
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                method, DistributedLock.class, false, false);
        if (attributes == null) {
            return invocation.proceed();
        }
        String key = attributes.getString("key");
        //获取锁种类
        LockType lockType = attributes.getEnum("lockType");
        //根据key生成lockKey
        String lockKey = this.convertLockKey(getArguments, getParameterTypes, annotations, key);
        return this.carriedLock(lockKey, lockType, invocation);

    }

    /**
     * 执行加锁的内容
     * @param lockKey
     * @param lockType
     * @param invocation
     * @return
     * @throws Throwable
     */

    private Object carriedLock(String lockKey, LockType lockType, MethodInvocation invocation) throws Throwable {
        IdistributedLock lock = new Dlock(lockKey, lockType);
        Object result = null;
        try {
            lock.lock();
            result = invocation.proceed();

        } catch (Exception e) {
            log.error("[base-common-distributedLock] Lock Fail: lockKey:{}", lockKey, e);
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * 根据用户传入的key生成lockey
     *
     * @param arguments
     * @param par
     * @param key
     * @param annotations
     * @return
     */
    private String convertLockKey(Object[] arguments, Class<?>[] par, Annotation[][] annotations, String key) {
        boolean flag = false;
        String lockKey = key;
        if (null == arguments || arguments.length < 1) {
            throw new RuntimeException("[base-common-distributedLock] can not find @LockKey");
        }
        try {
            for (int i = 0; i < arguments.length; i++) {
                Object arg = arguments[i];
                Class<?> aClass = par[i];
                Annotation[] paramAnnotation = annotations[i];
                for (Annotation an : paramAnnotation) {
                    if (!(an instanceof LockKey)) {
                        continue;
                    }
                    if (null == arg) {
                        log.warn("[base-common-distributedLock] @LockKey param value is null filed name: {}", aClass.getSimpleName());
                        continue;
                    }
                    flag = true;

                    lockKey = KeyUtils.getAnnotationKey(key, String.valueOf(arg));
                }
                for (Field field : aClass.getDeclaredFields()) {
                    LockKey annotation = field.getAnnotation(LockKey.class);
                    if (null == annotation) {
                        continue;
                    }

                    PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(field.getName(), aClass);
                    Object value = objPropertyDescriptor.getReadMethod().invoke(arg);

                    if (value == null) {
                        log.warn("[base-common-distributedLock] @LockKey Field value is null filed name: {}", field.getName());
                    } else {
                        flag = true;
                        lockKey = KeyUtils.getAnnotationKey(key, String.valueOf(value));
                    }
                }
            }
        } catch (Exception e) {
            log.error("[base-common-distributedLock] convertLockKey error:", e);
            return null;
        }

        if (flag) {
            return lockKey;
        } else {
            throw new RuntimeException("[base-common-distributedLock] can not find @LockKey");
        }

    }
}
