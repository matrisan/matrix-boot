//package com.matrixboot.redis.config.consumer;
//
//import com.matrixboot.redis.annotation.RedisStreamHandler;
//import com.matrixboot.redis.annotation.RedisStreamListener;
//import com.matrixboot.redis.annotation.RedisStreamListeners;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.aop.framework.Advised;
//import org.springframework.aop.support.AopUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.BeanInitializationException;
//import org.springframework.beans.factory.NoSuchBeanDefinitionException;
//import org.springframework.beans.factory.SmartInitializingSingleton;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.core.MethodIntrospector;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.AnnotatedElementUtils;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.util.Assert;
//import org.springframework.util.ReflectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * TODO
// * <p>
// * create in 2022/1/13 4:40 PM
// *
// * @author shishaodong
// * @version 0.0.1
// */
//@Slf4j
//public class RedisStreamListenerAnnotationBeanPostProcessor<K, V> implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {
//
//    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
//            Class<?> targetClass = AopUtils.getTargetClass(bean);
//            Collection<RedisStreamListener> classLevelListeners = findListenerAnnotations(targetClass);
//            final boolean hasClassLevelListeners = !classLevelListeners.isEmpty();
//            final List<Method> multiMethods = new ArrayList<>();
//            Map<Method, Set<RedisStreamListener>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
//                    (MethodIntrospector.MetadataLookup<Set<RedisStreamListener>>) method -> {
//                        Set<RedisStreamListener> listenerMethods = findListenerAnnotations(method);
//                        return (!listenerMethods.isEmpty() ? listenerMethods : null);
//                    });
//            if (hasClassLevelListeners) {
//                Set<Method> methodsWithHandler = MethodIntrospector.selectMethods(targetClass,
//                        (ReflectionUtils.MethodFilter) method ->
//                                AnnotationUtils.findAnnotation(method, RedisStreamHandler.class) != null);
//                multiMethods.addAll(methodsWithHandler);
//            }
//            if (annotatedMethods.isEmpty()) {
//                this.nonAnnotatedClasses.add(bean.getClass());
//                log.debug("No @RedisStreamListener annotations found on bean type: {}", bean.getClass());
//            } else {
//                // Non-empty set of methods
//                for (Map.Entry<Method, Set<RedisStreamListener>> entry : annotatedMethods.entrySet()) {
//                    Method method = entry.getKey();
//                    for (RedisStreamListener listener : entry.getValue()) {
//                        processRedisStreamListener(listener, method, bean, beanName);
//                    }
//                }
//                log.debug("@RedisStreamListener methods processed on bean '{}': {}", beanName, annotatedMethods);
//            }
//            if (hasClassLevelListeners) {
//                processMultiMethodListeners(classLevelListeners, multiMethods, bean, beanName);
//            }
//        }
//        return bean;
//    }
//
//
//    protected void processRedisStreamListener(RedisStreamListener kafkaListener, Method method, Object bean, String beanName) {
//        Method methodToUse = checkProxy(method, bean);
//        MethodKafkaListenerEndpoint<K, V> endpoint = new MethodKafkaListenerEndpoint<>();
//        endpoint.setMethod(methodToUse);
//        processListener(endpoint, kafkaListener, bean, methodToUse, beanName);
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//
//    }
//
//    @Override
//    public void afterSingletonsInstantiated() {
//
//    }
//
//
//    private void processMultiMethodListeners(Collection<RedisStreamListener> classLevelListeners, List<Method> multiMethods, Object bean, String beanName) {
//
//        List<Method> checkedMethods = new ArrayList<>();
//        Method defaultMethod = null;
//        for (Method method : multiMethods) {
//            Method checked = checkProxy(method, bean);
//            RedisStreamListener annotation = AnnotationUtils.findAnnotation(method, RedisStreamListener.class);
//            if (annotation != null && annotation.isDefault()) {
//                final Method toAssert = defaultMethod;
//                Assert.state(toAssert == null, () -> "Only one @KafkaHandler can be marked 'isDefault', found: " + toAssert.toString() + " and " + method.toString());
//                defaultMethod = checked;
//            }
//            checkedMethods.add(checked);
//        }
//        for (RedisStreamListener classLevelListener : classLevelListeners) {
//            MultiMethodKafkaListenerEndpoint<K, V> endpoint = new MultiMethodKafkaListenerEndpoint<>(checkedMethods, defaultMethod, bean);
//            processListener(endpoint, classLevelListener, bean, bean.getClass(), beanName);
//        }
//    }
//
//    private Method checkProxy(Method methodArg, Object bean) {
//        Method method = methodArg;
//        if (AopUtils.isJdkDynamicProxy(bean)) {
//            try {
//                // Found a @KafkaListener method on the target class for this JDK proxy ->
//                // is it also present on the proxy itself?
//                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
//                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
//                for (Class<?> iface : proxiedInterfaces) {
//                    try {
//                        method = iface.getMethod(method.getName(), method.getParameterTypes());
//                        break;
//                    } catch (@SuppressWarnings("unused") NoSuchMethodException noMethod) {
//                        // NOSONAR
//                    }
//                }
//            } catch (SecurityException ex) {
//                ReflectionUtils.handleReflectionException(ex);
//            } catch (NoSuchMethodException ex) {
//                throw new IllegalStateException(String.format(
//                        "@KafkaListener method '%s' found on bean target class '%s', " +
//                                "but not found in any interface(s) for bean JDK proxy. Either " +
//                                "pull the method up to an interface or switch to subclass (CGLIB) " +
//                                "proxies by setting proxy-target-class/proxyTargetClass " +
//                                "attribute to 'true'", method.getName(),
//                        method.getDeclaringClass().getSimpleName()), ex);
//            }
//        }
//        return method;
//    }
//
//
//    private @NotNull Collection<RedisStreamListener> findListenerAnnotations(Class<?> clazz) {
//        Set<RedisStreamListener> listeners = new HashSet<>();
//        RedisStreamListener ann = AnnotatedElementUtils.findMergedAnnotation(clazz, RedisStreamListener.class);
//        if (ann != null) {
//            listeners.add(ann);
//        }
//        RedisStreamListeners anns = AnnotationUtils.findAnnotation(clazz, RedisStreamListeners.class);
//        if (anns != null) {
//            listeners.addAll(Arrays.asList(anns.value()));
//        }
//        return listeners;
//    }
//
//    private Set<RedisStreamListener> findListenerAnnotations(Method method) {
//        Set<RedisStreamListener> listeners = new HashSet<>();
//        RedisStreamListener ann = AnnotatedElementUtils.findMergedAnnotation(method, RedisStreamListener.class);
//        if (ann != null) {
//            listeners.add(ann);
//        }
//        RedisStreamListeners anns = AnnotationUtils.findAnnotation(method, RedisStreamListeners.class);
//        if (anns != null) {
//            listeners.addAll(Arrays.asList(anns.value()));
//        }
//        return listeners;
//    }
//
//    protected void processListener(MethodKafkaListenerEndpoint<?, ?> endpoint, KafkaListener kafkaListener,
//                                   Object bean, Object adminTarget, String beanName) {
//
//        String beanRef = kafkaListener.beanRef();
//        if (StringUtils.hasText(beanRef)) {
//            this.listenerScope.addListener(beanRef, bean);
//        }
//        endpoint.setBean(bean);
//        endpoint.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);
//        endpoint.setId(getEndpointId(kafkaListener));
//        endpoint.setGroupId(getEndpointGroupId(kafkaListener, endpoint.getId()));
//        endpoint.setTopicPartitions(resolveTopicPartitions(kafkaListener));
//        endpoint.setTopics(resolveTopics(kafkaListener));
//        endpoint.setTopicPattern(resolvePattern(kafkaListener));
//        endpoint.setClientIdPrefix(resolveExpressionAsString(kafkaListener.clientIdPrefix(), "clientIdPrefix"));
//        String group = kafkaListener.containerGroup();
//        if (StringUtils.hasText(group)) {
//            Object resolvedGroup = resolveExpression(group);
//            if (resolvedGroup instanceof String) {
//                endpoint.setGroup((String) resolvedGroup);
//            }
//        }
//        String concurrency = kafkaListener.concurrency();
//        if (StringUtils.hasText(concurrency)) {
//            endpoint.setConcurrency(resolveExpressionAsInteger(concurrency, "concurrency"));
//        }
//        String autoStartup = kafkaListener.autoStartup();
//        if (StringUtils.hasText(autoStartup)) {
//            endpoint.setAutoStartup(resolveExpressionAsBoolean(autoStartup, "autoStartup"));
//        }
//        resolveKafkaProperties(endpoint, kafkaListener.properties());
//        endpoint.setSplitIterables(kafkaListener.splitIterables());
//
//        KafkaListenerContainerFactory<?> factory = null;
//        String containerFactoryBeanName = resolve(kafkaListener.containerFactory());
//        if (StringUtils.hasText(containerFactoryBeanName)) {
//            Assert.state(this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name");
//            try {
//                factory = this.beanFactory.getBean(containerFactoryBeanName, KafkaListenerContainerFactory.class);
//            }
//            catch (NoSuchBeanDefinitionException ex) {
//                throw new BeanInitializationException("Could not register Kafka listener endpoint on [" + adminTarget
//                        + "] for bean " + beanName + ", no " + KafkaListenerContainerFactory.class.getSimpleName()
//                        + " with id '" + containerFactoryBeanName + "' was found in the application context", ex);
//            }
//        }
//
//        endpoint.setBeanFactory(this.beanFactory);
//        String errorHandlerBeanName = resolveExpressionAsString(kafkaListener.errorHandler(), "errorHandler");
//        if (StringUtils.hasText(errorHandlerBeanName)) {
//            endpoint.setErrorHandler(this.beanFactory.getBean(errorHandlerBeanName, KafkaListenerErrorHandler.class));
//        }
//        this.registrar.registerEndpoint(endpoint, factory);
//        if (StringUtils.hasText(beanRef)) {
//            this.listenerScope.removeListener(beanRef);
//        }
//    }
//
//    @Override
//    public int getOrder() {
//        return LOWEST_PRECEDENCE;
//    }
//}
