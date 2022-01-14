package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.annotation.AccessLimit;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
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

    @Resource
    private IAccessLimitService accessLimitService;

    private BeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Map<Method, AccessLimitMeta> anno = findAnno(bean.getClass());
        if (anno.isEmpty()) {
            return bean;
        }
        return new ProxyFactory(bean, anno, accessLimitService).getProxyInstance();
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        accessLimitService = beanFactory.getBean(IAccessLimitService.class);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private @NotNull Map<Method, AccessLimitMeta> findAnno(@NotNull Class<?> clz) {
        Map<Method, AccessLimitMeta> map = new HashMap<>(6);
        for (Method method : clz.getMethods()) {
            AccessLimit limit = AnnotationUtils.findAnnotation(method, AccessLimit.class);
            if (!Objects.isNull(limit)) {
                map.put(method, new AccessLimitMeta(limit, method));
            }
        }
        return map;
    }
}
