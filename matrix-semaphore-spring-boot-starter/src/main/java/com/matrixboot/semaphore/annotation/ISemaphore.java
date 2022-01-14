package com.matrixboot.semaphore.annotation;

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

    boolean tryAcquire(SemaphoreMeta meta);

    void release(String key);

}
