package com.xiaoying.base.lock.annotation.aop;

import com.xiaoying.base.lock.annotation.DistributedLock;
import org.springframework.aop.ClassFilter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class LockClassFilter implements ClassFilter {
    @Override
    public boolean matches(Class<?> clazz) {
        boolean isMatch = false;
        Class targetClass = clazz;
        if (ClassUtils.isCglibProxyClass(clazz)) {
            targetClass = clazz.getSuperclass();
        }
        List<Method> methods = new ArrayList<>();
        ReflectionUtils.doWithMethods(targetClass, methods::add, method ->
                Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()));
        for (Method method : methods) {
            AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                    method, DistributedLock.class, false, false);
            if (attributes != null) {
                isMatch = true;
            }
            if (isMatch) {
                break;
            }
        }
        return isMatch;
    }
}

