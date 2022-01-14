package com.matrixboot.idempotent.annotation;

/**
 * <p>
 * create in 2022/1/12 3:13 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@FunctionalInterface
public interface IIdempotentHook {

    /**
     * 钩子函数
     */
    void invoke();

}
