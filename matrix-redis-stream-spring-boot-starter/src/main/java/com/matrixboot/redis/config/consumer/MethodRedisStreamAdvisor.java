package com.matrixboot.redis.config.consumer;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * TODO
 * <p>
 * create in 2022/1/13 3:17 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
@Setter
public class MethodRedisStreamAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private static final long serialVersionUID = 4760806076805785861L;

    private transient BeanFactory beanFactory;

    private RedisStreamOperationSource redisStreamOperationSource;

    private transient Advice advice;

    @Setter
    private transient RedisStreamAttributeSourcePointcut pointcut = new RedisStreamAttributeSourcePointcut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
