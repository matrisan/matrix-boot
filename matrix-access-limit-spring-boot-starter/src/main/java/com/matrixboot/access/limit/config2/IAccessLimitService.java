package com.matrixboot.access.limit.config2;

import com.matrixboot.access.limit.config.AccessLimitMeta;

import java.lang.reflect.Method;

/**
 * TODO
 * <p>
 * create in 2022/1/14 4:58 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@FunctionalInterface
public interface IAccessLimitService {


    void check(Method method, Object[] args, AccessLimitMeta meta);

}
