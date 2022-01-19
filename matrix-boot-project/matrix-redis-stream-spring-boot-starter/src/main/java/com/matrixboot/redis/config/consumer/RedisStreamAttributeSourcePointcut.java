package com.matrixboot.redis.config.consumer;

import com.matrixboot.redis.annotation.RedisStreamListener;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.log.LogMessage;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p>
 * create in 2022/1/13 3:19 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public class RedisStreamAttributeSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

    private static final long serialVersionUID = 1644901862091781332L;

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        RedisStreamListener annotation = findAnnotation(method, targetClass, RedisStreamListener.class);
        return !Objects.isNull(annotation);
    }

    private <A extends Annotation> @Nullable A findAnnotation(Method method, Class<?> targetClass, Class<A> annotationClass) {
        // The method may be on an interface, but we need attributes from the target
        // class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        A annotation = AnnotationUtils.findAnnotation(specificMethod, annotationClass);
        if (annotation != null) {
            log.debug(String.valueOf(LogMessage.format("%s found on specific method: %s", annotation, specificMethod)));
            return annotation;
        }
        // Check the original (e.g. interface) method
        if (specificMethod != method) {
            annotation = AnnotationUtils.findAnnotation(method, annotationClass);
            if (annotation != null) {
                log.debug(String.valueOf(LogMessage.format("%s found on: %s", annotation, method)));
                return annotation;
            }
        }
        // Check the class-level (note declaringClass, not targetClass, which may not
        // actually implement the method)
        annotation = AnnotationUtils.findAnnotation(specificMethod.getDeclaringClass(), annotationClass);
        if (annotation != null) {
            log.debug(String.valueOf(LogMessage.format("%s found on: %s", annotation, specificMethod.getDeclaringClass().getName())));
            return annotation;
        }
        return null;
    }

}
