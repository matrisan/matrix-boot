package com.matrixboot.redis.config.producer;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * create in 2021/12/15 12:15 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public interface InvocationHandlerFactory {

    InvocationHandler create(Target<?> target, Map<Method, MethodHandler> dispatch);

    /**
     * Like {@link InvocationHandler#invoke(Object, Method, Object[])}, except for a
     * single method.
     */
    interface MethodHandler {

        Object invoke(Object[] argv) throws Throwable;

    }

    class Default implements InvocationHandlerFactory {

        @Override
        public InvocationHandler create(Target<?> target, Map<Method, MethodHandler> dispatch) {
            return new RedisStreamInvocationHandler(target, dispatch);
        }
    }

}
