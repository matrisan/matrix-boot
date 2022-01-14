package com.matrixboot.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis.stream 配置文件
 * <p>
 * create in 2022/1/13 2:53 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
@Component
@ConfigurationProperties("com.matrixboot.redis.stream")
public class RedisStreamProperties {

    private Integer defaultPullSize;

    public Integer getDefaultPullSize() {
        if (null == defaultPullSize) {
            return 10;
        }
        return defaultPullSize;
    }
}
