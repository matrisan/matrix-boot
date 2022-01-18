package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.annotation.AccessLimit;
import com.matrixboot.access.limit.annotation.AccessLimits;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * create in 2022/1/14 4:15 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class AccessLimitAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Map<Method, IAccessLimitService> anno = findAnnotations(bean.getClass());
        if (anno.isEmpty()) {
            return bean;
        }
        return new ProxyFactory(bean, anno).getProxyInstance();
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    @Override
    public int getOrder() {
        return 0;
    }

    private @NotNull Map<Method, IAccessLimitService> findAnnotations(@NotNull Class<?> clz) {
        Map<Method, IAccessLimitService> container = new HashMap<>(6);
        for (Method method : clz.getMethods()) {
            if (findMany(method, container)) {
                continue;
            }
            findSingle(method, container);
        }
        return container;
    }

    private void findSingle(Method method, Map<Method, IAccessLimitService> container) {
        AccessLimit accessLimit = AnnotationUtils.findAnnotation(method, AccessLimit.class);
        if (!Objects.isNull(accessLimit)) {
            container.put(method, getAccessLimitService(accessLimit));
        }
    }

    private boolean findMany(Method method, Map<Method, IAccessLimitService> container) {
        AccessLimits accessLimits = AnnotationUtils.findAnnotation(method, AccessLimits.class);
        if (Objects.isNull(accessLimits)) {
            // 没有找到 AccessLimits, 需要找 AccessLimit
            return false;
        }
        container.put(method, getAccessLimitService(accessLimits));
        return true;
    }

    private @NotNull IAccessLimitService getAccessLimitService(AccessLimit accessLimit) {
        AccessLimitRedisServiceImpl service = new AccessLimitRedisServiceImpl(accessLimit);
        init(service);
        return service;
    }

    private @NotNull IAccessLimitService getAccessLimitService(AccessLimits accessLimits) {
        AccessLimitRedisServiceImpl service = new AccessLimitRedisServiceImpl(accessLimits);
        init(service);
        return service;
    }

    @SuppressWarnings("all")
    @SneakyThrows
    public void init(@NotNull IAccessLimitService service) {
        service.setExpressionParser(beanFactory.getBean(ExpressionParser.class));
        service.setParameterNameDiscoverer(beanFactory.getBean(ParameterNameDiscoverer.class));
        service.setStringRedisTemplate(beanFactory.getBean(StringRedisTemplate.class));
        service.setBeanResolver(beanFactory.getBean(BeanResolver.class));
        service.setRedisScript((RedisScript<Boolean>) beanFactory.getBean("redisScript"));
        service.setAccessLimitProperties(beanFactory.getBean(AccessLimitProperties.class));
        service.afterPropertiesSet();
    }

    @Override
    public void afterSingletonsInstantiated() {

    }
}
