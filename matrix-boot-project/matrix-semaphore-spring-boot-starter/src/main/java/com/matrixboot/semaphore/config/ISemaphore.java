package com.matrixboot.semaphore.config;

/**
 * <p>
 * create in 2021/12/29 2:31 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public interface ISemaphore {


    /**
     * 获取
     *
     * @param meta SemaphoreMeta
     */
    void acquire(SemaphoreMeta meta);

    /**
     * 获取
     *
     * @param meta SemaphoreMeta
     * @return boolean
     */
    boolean tryAcquire(SemaphoreMeta meta);

    /**
     * 释放锁
     *
     * @param key key
     */
    void release(String key);

}
