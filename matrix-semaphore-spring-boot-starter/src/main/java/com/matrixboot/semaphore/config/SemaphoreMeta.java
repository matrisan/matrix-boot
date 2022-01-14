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

    private final Semaphore brake;

    private final SemaphoreProperties properties;

    public SemaphoreMeta(Semaphore brake, SemaphoreProperties properties) {
        this.brake = brake;
        this.properties = properties;
    }

    public String getKey() {
        if (!StringUtils.hasText(properties.getPrefix())) {
            return "com:matrixboot:brake:" + brake.value();
        }
        return properties.getPrefix() + ":" + brake.value();
    }

    public long getTimeout(){
        return brake.timeout();
    }

    public TimeUnit getTimeUnit(){
        return brake.unit();
    }

}
