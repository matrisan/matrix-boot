package com.matrixboot.semaphore.annotation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * create in 2021/12/29 2:48 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "com.matrixboot.brake")
public class SemaphoreProperties {

    private String prefix;






}
