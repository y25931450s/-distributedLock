package com.xiaoying.base.lock.annotation.autoconfig;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LockNameHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("enable", new LockParser());
    }
}
