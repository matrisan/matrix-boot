package com.matrixboot.redis.config.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * create in 2022/1/13 3:14 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public class RedisStreamAspectSupport implements BeanFactoryAware, InitializingBean {

    protected BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
