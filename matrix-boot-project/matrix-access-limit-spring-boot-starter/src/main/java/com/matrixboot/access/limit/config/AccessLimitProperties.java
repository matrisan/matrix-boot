package com.matrixboot.access.limit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * create in 2022/1/14 3:47 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.matrixboot.access.limit")
public class AccessLimitProperties {

    /**
     * 默认的存储的前缀
     */
    private String prefix = "com:matrixboot:access:limit:";

}
