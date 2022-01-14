package com.matrixboot.semaphore.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>
 * create in 2021/12/29 2:32 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@AllArgsConstructor
public class SemaphoreRedisImpl implements ISemaphore {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @SneakyThrows(InterruptedException.class)
    public void acquire(String key) {
        boolean flag = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, ""));
        while (Boolean.FALSE.equals(flag)) {
            Thread.sleep(0);
            flag = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, ""));
        }
    }

    @Override
    public void release(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public boolean tryAcquire(@NotNull SemaphoreMeta meta) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(
                meta.getKey(), "",
                meta.getTimeout(),
                meta.getTimeUnit()));
    }
}
