package com.matrixboot.brake.annotation;

/**
 * TODO
 * <p>
 * create in 2021/12/29 2:31 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public interface ISemaphore {

    @Deprecated
    void acquire(String key);

    boolean tryAcquire(BrakeMeta meta);

    void release(String key);

}
