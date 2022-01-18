package com.matrixboot.semaphore.config;

import com.matrixboot.semaphore.annotation.Semaphore;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * create in 2021/12/29 2:53 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class SemaphoreMeta {

    private final Semaphore semaphore;

    private final SemaphoreProperties properties;

    public SemaphoreMeta(Semaphore semaphore, SemaphoreProperties properties) {
        this.semaphore = semaphore;
        this.properties = properties;
    }

    public String getKey() {
        if (!StringUtils.hasText(properties.getPrefix())) {
            return "com:matrixboot:brake:" + semaphore.value();
        }
        return properties.getPrefix() + ":" + semaphore.value();
    }

    public long getTimeout(){
        return semaphore.timeout();
    }

    public TimeUnit getTimeUnit(){
        return semaphore.unit();
    }

}
