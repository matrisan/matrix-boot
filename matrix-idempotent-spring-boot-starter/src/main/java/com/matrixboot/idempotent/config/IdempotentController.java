package com.matrixboot.idempotent.config;

import cn.hutool.core.util.IdUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.matrixboot.idempotent.config.IdempotentCommon.BLANK_VALUE;

/**
 * <p>
 * create in 2022/1/12 11:17 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@RestController
@RequestMapping("/matrixboot/idempotent")
public class IdempotentController {

    @Resource
    private IdempotentProperties properties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/token")
    public String token() {
        String id = IdUtil.objectId();
        stringRedisTemplate.opsForValue().setIfAbsent(getRedisKey(id), BLANK_VALUE, properties.getTimeout(), properties.getUnit());
        return id;
    }

    private @NotNull String getRedisKey(String id) {
        return properties.getRedisKeyPrefix() + id;
    }
}
