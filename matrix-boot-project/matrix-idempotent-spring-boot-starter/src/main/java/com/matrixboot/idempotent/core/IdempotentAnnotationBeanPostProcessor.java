package com.matrixboot.idempotent.core;

import com.matrixboot.idempotent.annotation.Idempotent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * <p>
 * create in 2022/1/24 3:39 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class IdempotentAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Map<Method, IdempotentMeta> idempotentMap = findAnnotations(bean.getClass());
        return CollectionUtils.isEmpty(idempotentMap) ? bean : new IdempotentProxyFactory(bean, idempotentMap, beanFactory.getBean(AbstractIdempotentService.class))
                .getProxyInstance();
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private @NotNull Map<Method, IdempotentMeta> findAnnotations(@NotNull Class<?> clz) {
        Map<Method, IdempotentMeta> map = new HashMap<>(8);
        for (Method method : clz.getMethods()) {
            Idempotent annotation = AnnotationUtils.findAnnotation(method, Idempotent.class);
            if (!Objects.isNull(annotation)) {
                map.put(method, new IdempotentMeta(annotation));
            }
        }
        return map;
    }

    @Override
    public void afterSingletonsInstantiated() {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
