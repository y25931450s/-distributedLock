package com.xiaoying.base.lock.annotation.autoconfig;

import com.xiaoying.base.lock.annotation.aop.LockAopCreator;
import com.xiaoying.base.lock.config.RedisConfig;
import com.xiaoying.base.lock.config.ZkConfig;
import com.xiaoying.base.lock.util.BeanNameConstant;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class LockConfigRegistrar implements ImportBeanDefinitionRegistrar {

    private void initLockCreatorBean(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BeanNameConstant.LOCK_AOP_CREATOR_BEANNAME)) {
            RedisConfig.getRedisClient();
            ZkConfig.getZkClient();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(LockAopCreator.class);
            beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
            registry.registerBeanDefinition(BeanNameConstant.LOCK_AOP_CREATOR_BEANNAME,
                    beanDefinitionBuilder.getBeanDefinition());

        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        initLockCreatorBean(registry);
    }
}
