package com.xiaoying.base.lock.annotation.autoconfig;

import com.xiaoying.base.lock.annotation.aop.LockAopCreator;
import com.xiaoying.base.lock.config.RedisConfig;
import com.xiaoying.base.lock.config.ZkConfig;
import com.xiaoying.base.lock.util.BeanNameConstant;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class LockParser extends AbstractBeanDefinitionParser {
    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        // 设置beanName
        element.setAttribute("id", BeanNameConstant.LOCK_AOP_CREATOR_BEANNAME);
        // 检查是否重复使用lock:enable
        if (parserContext.getRegistry().containsBeanDefinition(BeanNameConstant.LOCK_AOP_CREATOR_BEANNAME)) {
            throw new BeanCreationException("[Lock]-[customAnnotation] Please don't repeat use the <Lock:enable/>");
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(LockAopCreator.class);
        beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinitionBuilder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        RedisConfig.getRedisClient();
        ZkConfig.getZkClient();
        return beanDefinitionBuilder.getBeanDefinition();
    }
}
