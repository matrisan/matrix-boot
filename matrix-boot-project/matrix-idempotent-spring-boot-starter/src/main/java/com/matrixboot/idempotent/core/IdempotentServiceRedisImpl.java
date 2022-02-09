package com.matrixboot.idempotent.core;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * <p>
 * create in 2022/1/24 3:27 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public class IdempotentServiceRedisImpl extends AbstractIdempotentService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "redisScriptIdempotentCheck")
    private RedisScript<Boolean> redisScriptIdempotentCheck;

    @Resource(name = "redisScriptIdempotentInsert")
    private RedisScript<Boolean> redisScriptIdempotentInsert;

    @Override
    protected String getSubToken() {
        return IdUtil.objectId();
    }

    @Override
    protected boolean persistence(String finalToken, String value) {
        return Boolean.TRUE.equals(stringRedisTemplate.execute(redisScriptIdempotentInsert, Collections.singletonList(finalToken), value));
    }

    @Override
    public boolean doCheck(IdempotentMeta idempotent) {
        String token = getRequestToken(idempotent);
        if (!StringUtils.hasText(token)) {
            log.debug("header 中没有 token, 直接返回 true 进行拦截");
            return true;
        }
        boolean equals = Boolean.TRUE.equals(stringRedisTemplate.execute(redisScriptIdempotentCheck, Collections.singletonList(token)));
        log.debug("本次请求,幂等匹配结果 - {}", equals);
        return equals;
    }

}
