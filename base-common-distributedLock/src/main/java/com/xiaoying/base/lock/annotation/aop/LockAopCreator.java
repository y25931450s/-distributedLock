package com.xiaoying.base.lock.annotation.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

public class LockAopCreator extends AbstractAutoProxyCreator {

    private LockAdvisor lockAdvisor;

    public LockAopCreator() {
        this.lockAdvisor = new LockAdvisor();
    }


    /**
     * 是否需要跳过（不跳过会导致生成很多代理类，可能导致系统启动发生错误）
     *
     * @param beanClass
     * @param beanName
     * @return
     */
    @Override
    protected boolean shouldSkip(Class<?> beanClass, String beanName) {
        Pointcut pointcut = lockAdvisor.getPointcut();
        return !pointcut.getClassFilter().matches(beanClass);
    }

    @Override
    protected void customizeProxyFactory(ProxyFactory proxyFactory) {
        proxyFactory.setOptimize(true);
        proxyFactory.setProxyTargetClass(true);
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[]{
                lockAdvisor
        };
    }

}
