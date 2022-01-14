package com.matrixboot.access.limit.config;

/**
 * 回调接口
 * <p>
 * create in 2022/1/14 4:07 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@FunctionalInterface
public interface IAccessLimitCallback {

    /**
     * 回调接口
     */
    void invoke();

}
