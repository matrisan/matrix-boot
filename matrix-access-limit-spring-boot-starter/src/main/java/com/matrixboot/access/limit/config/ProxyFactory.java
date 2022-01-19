package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.dto.AccessLimitResult;
import com.matrixboot.access.limit.exception.AccessLimitException;
import org.jetbrains.annotations.NotNull;
import org.joor.Reflect;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * create in 2022/1/14 4:32 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class ProxyFactory implements MethodInterceptor {

    private final Object target;

    private final Map<Method, IAccessLimitService> anno;

    public ProxyFactory(Object target, Map<Method, IAccessLimitService> anno) {
        this.target = target;
        this.anno = anno;
    }

    /**
     * 为目标对象生成代理对象
     *
     * @return Object
     */
    public Object getProxyInstance() {
        //工具类
        Enhancer en = new Enhancer();
        //设置父类
        en.setSuperclass(target.getClass());
        //设置回调函数
        en.setCallback(this);
        //创建子类对象代理
        return en.create();
    }

    @Override
    public Object intercept(Object o, @NotNull Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        IAccessLimitService accessLimitMeta = anno.get(method);
        // 执行真正的方法之前,先执行自定的方法
        AccessLimitResult accessLimitResult = accessLimitMeta.doCheck(method, args);
        if (Boolean.FALSE.equals(accessLimitResult.getResult())) {
            // 不需要拦截直接返回
            return method.invoke(target, args);
        }
        // 看看是否自定义了方法,如果有就调用,方法参数和真正的方法参数是一样的
        if (StringUtils.hasText(accessLimitMeta.getReveal())) {
            Class<?>[] argsTypes = getMethod(accessLimitMeta.getReveal());
            Map<Class<?>, Object> map = argsToMap(args);
            map.put(AccessLimitException.class, new AccessLimitException(accessLimitResult.getMessage()));
            return Reflect.on(target).call(accessLimitMeta.getReveal(), assembleArgs(argsTypes, map)).get();
        } else {
            // 没有定义回调方法直接抛出异常
            throw new AccessLimitException(accessLimitResult.getMessage());
        }
    }

    private Object @NotNull [] assembleArgs(Class<?> @NotNull [] parameterTypes, Map<Class<?>, Object> map) {
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = map.get(parameterTypes[i]);
        }
        return args;
    }

    private @NotNull Map<Class<?>, Object> argsToMap(Object @NotNull [] args) {
        Map<Class<?>, Object> map = new HashMap<>(args.length + 3);
        for (Object arg : args) {
            map.put(arg.getClass(), arg);
        }
        return map;
    }

    private Class<?>[] revealParams;

    /**
     * 方法参数的类型比较固定,第一次运行时候缓存下来
     *
     * @param methodName 方法名称
     * @return Class []
     */
    private Class<?> @NotNull [] getMethod(String methodName) {
        if (Objects.isNull(revealParams)) {
            Method[] methods = target.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    revealParams = method.getParameterTypes();
                }
            }
        }
        return revealParams;
    }
}
