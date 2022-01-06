package com.matrixboot.brake.annotation;

import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * create in 2021/12/29 2:53 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class BrakeMeta {

    private final Brake brake;

    private final BrakeProperties properties;

    public BrakeMeta(Brake brake, BrakeProperties properties) {
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
