package com.xiaoying.base.lock.annotation.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

public class LockAdvisor extends AbstractPointcutAdvisor {

    private LockAttributeSourcePointcut lockAttributeSourcePointcut;

    private LockInterceptor lockInterceptor;


    LockAdvisor() {

        this.lockAttributeSourcePointcut = new LockAttributeSourcePointcut();
        this.lockInterceptor = new LockInterceptor();
        //设置aop的顺序
        setOrder(3);

    }


    @Override
    public Pointcut getPointcut() {
        return lockAttributeSourcePointcut;
    }

    @Override
    public Advice getAdvice() {
        return lockInterceptor;
    }


}
