package com.matrixboot.semaphore.config;

import cn.hutool.core.net.NetUtil;
import com.matrixboot.semaphore.exception.OutOfCycleSemaphoreException;
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

    private final SemaphoreProperties properties;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void release(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    @SuppressWarnings("all")
    @SneakyThrows(InterruptedException.class)
    public void acquire(SemaphoreMeta meta) {
        int count = 0;
        while (!tryAcquire(meta)) {
            count++;
            Thread.sleep(0);
            if (count == properties.getCycle()) {
                throw new OutOfCycleSemaphoreException("超过自旋次数");
            }
        }
    }


    @Override
    public boolean tryAcquire(@NotNull SemaphoreMeta meta) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(
                meta.getKey(),
                NetUtil.getLocalhost().getHostAddress(),
                meta.getTimeout(),
                meta.getTimeUnit()
        ));
    }
}
