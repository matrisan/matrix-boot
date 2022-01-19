package com.matrixboot.redis.config.producer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * create in 2021/12/15 4:24 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisStreamEndpointMeta {

    private String value;

    private String serializer;

}
