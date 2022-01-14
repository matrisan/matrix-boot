package com.matrixboot.redis.config.producer;


import com.matrixboot.redis.annotation.RedisStreamEndpoint;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * create in 2021/12/14 10:52 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class RedisStreamClientFactoryBean implements FactoryBean<Object>, InitializingBean, BeanFactoryAware {

    private Class<?> type;

    private final InvocationHandlerFactory factory = new InvocationHandlerFactory.Default();

    private BeanFactory beanFactory;

    @Override
    public Object getObject() {
        Target<?> target = new HardCodedTarget<>();
        InvocationHandler handler = factory.create(target, getMethodHandler());
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private @NotNull Map<Method, InvocationHandlerFactory.MethodHandler> getMethodHandler() {
        Map<Method, InvocationHandlerFactory.MethodHandler> map = new HashMap<>();
        for (Method method : type.getMethods()) {
            if (method.isAnnotationPresent(RedisStreamEndpoint.class)) {
                RedisStreamEndpoint annotation = method.getAnnotation(RedisStreamEndpoint.class);
                map.put(method, new DefaultMethodHandler(new RedisStreamEndpointMeta(annotation.value(), annotation.encode()), beanFactory));
            }
        }
        return map;
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

}
