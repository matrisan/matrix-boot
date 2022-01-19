package com.matrixboot.idempotent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * create in 2022/1/12 2:09 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.matrixboot.idempotent")
public class IdempotentProperties {

    private String redisKeyPrefix = "com:matrixboot:idempotent:";

    private Long timeout = 1L;

    private TimeUnit unit = TimeUnit.DAYS;

}
