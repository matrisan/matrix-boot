package com.matrixboot.access.limit.config2;

import com.matrixboot.access.limit.config.AccessLimitException;
import com.matrixboot.access.limit.config.AccessLimitMeta;
import org.jetbrains.annotations.NotNull;
import org.joor.Reflect;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * create in 2022/1/14 4:32 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class ProxyFactory implements MethodInterceptor {

    private final Object target;
    private final Map<Method, AccessLimitMeta> anno;
    private final IAccessLimitService service;

    public ProxyFactory(Object target, Map<Method, AccessLimitMeta> anno, IAccessLimitService service) {
        this.target = target;
        this.anno = anno;
        this.service = service;
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
    public Object intercept(Object o, @NotNull Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        AccessLimitMeta accessLimitMeta = anno.get(method);
        try {
            service.check(method, objects, accessLimitMeta);
            return method.invoke(target, objects);
        } catch (AccessLimitException exception) {
            if (StringUtils.hasText(accessLimitMeta.getReveal())) {
                return Reflect.on(target).call(accessLimitMeta.getReveal(), objects).get();
            } else {
                throw new AccessLimitException("请求速率收到限制!");
            }
        }
    }
}
