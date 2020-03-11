package com.xiaoying.base.lock.annotation.aop;

import com.xiaoying.base.lock.annotation.DistributedLock;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.io.Serializable;
import java.lang.reflect.Method;

public class LockAttributeSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

    /**
     * 判断类是否有DistributedLock注解
     */
    private LockClassFilter lockClassFilter;


    public LockAttributeSourcePointcut() {
        this.lockClassFilter = new LockClassFilter();
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {

        if (method.getDeclaringClass() == Object.class) {
            return false;
        }
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                method, DistributedLock.class, false, false);
        if (attributes != null) {
            return true;
        }
        return false;
    }

    @Override
    public ClassFilter getClassFilter() {
        return this.lockClassFilter;
    }
}
