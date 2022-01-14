package com.matrixboot.access.limit.config;

import java.lang.reflect.Method;

/**
 * <p>
 * create in 2022/1/14 4:58 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@FunctionalInterface
public interface IAccessLimitService {


    /**
     * 真正执行 check 的方法
     *
     * @param method 待执行的方法
     * @param args   方法的参数
     * @param meta   注解元数据
     */
    void doCheck(Method method, Object[] args, AccessLimitMeta meta);

}
