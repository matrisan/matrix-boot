package com.matrixboot.idempotent.config;

import cn.hutool.core.net.NetUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * create in 2022/1/12 2:14 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdempotentCommon {

    public static final String BLANK_VALUE = "";

    public static @NotNull String getRedisKey(@NotNull IdempotentProperties properties, String token) {
        return properties.getRedisKeyPrefix() + token;
    }

    public static String getLocalhost(){
        return NetUtil.getLocalhost().getHostAddress();
    }

}
