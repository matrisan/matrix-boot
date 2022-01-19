package com.matrixboot.redis.config.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * create in 2022/1/13 3:16 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public class RedisStreamInterceptor extends RedisStreamAspectSupport implements MethodInterceptor, Serializable, InitializingBean {

    private static final long serialVersionUID = -855585676738056316L;

    @Resource
    private ObjectMapper objectMapper;


    private ArrayDeque<?> arrayBlockingQueue = new ArrayDeque<>(100);


    private Map<Method, ArrayDeque<?>> arrayDequeMap = new HashMap<>();

    private String name;

    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        Assert.isTrue(arguments.length == 1, "数据长度不符合!");
        Object argument = arguments[0];
        name = invocation.getMethod().getName();
        Class<?> aClass = argument.getClass();
        Method method = invocation.getMethod();
        return invocation.proceed();
    }

    private void async() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.error("RedisStreamInterceptor - {}", name);
    }
}
