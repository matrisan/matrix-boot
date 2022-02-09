package com.matrixboot.idempotent.core;

import com.matrixboot.idempotent.exception.RequestIdempotentException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joor.Reflect;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * create in 2022/1/24 3:53 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public class IdempotentProxyFactory implements MethodInterceptor {

    private final Object target;

    private final Map<Method, IdempotentMeta> anno;

    private final AbstractIdempotentService idempotentService;

    public IdempotentProxyFactory(Object target, Map<Method, IdempotentMeta> methodIdempotent, AbstractIdempotentService idempotentService) {
        this.target = target;
        this.anno = methodIdempotent;
        this.idempotentService = idempotentService;
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
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 不存在注解,不需要增强
        if (!anno.containsKey(method)) {
            log.trace("当前方法不存在幂等注解 - {}", method.getName());
            return method.invoke(target, args);
        }
        IdempotentMeta idempotent = anno.get(method);
        if (idempotentService.doCheck(idempotent)) {
            log.debug("该方法需要幂等拦截 - {}", method.getName());
            String recover = idempotent.getRecover();
            if (StringUtils.hasText(recover)) {
                log.debug("该方法需要幂调用回调方法 - {} - {}", method.getName(), recover);
                return Reflect.on(target).call(recover, assembleRecoverArgs(recover, args, idempotent)).get();
            } else {
                log.debug("该方法没有回调方法 - {} 进行异常抛出", method.getName());
                throw new RequestIdempotentException(idempotent.getMessage());
            }
        } else {
            return method.invoke(target, args);
        }
    }


    /**
     * 原函数和回调函数的方法参数并非一一对应, 包括参数个数,参数的类型,参数的顺序
     *
     * @param methodName 回调方法
     * @param args       方法采纳数
     * @param idempotent 幂等注解的元数据
     * @return Object[]
     */
    private Object @NotNull [] assembleRecoverArgs(String methodName, Object @NotNull [] args, @NotNull IdempotentMeta idempotent) {
        Class<?>[] recoverMethodParamTypes = getRecoverMethodParamTypes(methodName);
        Map<Class<?>, Object> requestArgsMap = requestArgsToMap(args);
        requestArgsMap.put(RequestIdempotentException.class, new RequestIdempotentException(idempotent.getMessage()));
        return assembleArgs(recoverMethodParamTypes, requestArgsMap);
    }

    private Class<?>[] revealParams;

    /**
     * 根据 方法名称获取所有的方法参数类型
     * 方法参数的类型比较固定,第一次运行时候缓存下来
     *
     * @param methodName 方法名称
     * @return Class
     */
    private Class<?>[] getRecoverMethodParamTypes(String methodName) {
        if (Objects.isNull(revealParams)) {
            Method[] methods = target.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    revealParams = method.getParameterTypes();
                }
            }
        }
        Assert.isTrue(!Objects.isNull(revealParams), "参数的类型数组不能为空!");
        return revealParams;
    }

    /**
     * 将实时请求的方法参数转换成 map, key 是参数类型, value 是参数值
     *
     * @param args 实时请求的方法参数
     * @return map
     */
    private @NotNull Map<Class<?>, Object> requestArgsToMap(Object @NotNull [] args) {
        Map<Class<?>, Object> map = new HashMap<>(args.length + 3);
        for (Object arg : args) {
            map.put(arg.getClass(), arg);
        }
        return map;
    }

    /**
     * 将实时请求的参数与 回调方法参数进行对应
     *
     * @param parameterTypes 方法参数类型
     * @param map            实时请求的方法参数
     * @return Object[]
     */
    private Object @NotNull [] assembleArgs(Class<?> @NotNull [] parameterTypes, Map<Class<?>, Object> map) {
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = map.get(parameterTypes[i]);
        }
        return args;
    }

}
