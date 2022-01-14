package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.annotation.AccessLimit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * <p>
 * create in 2021/12/16 11:17 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccessLimitMeta {

    String value;

    String reveal;

    Integer times;

    Integer timeout;

    String methodName;

    public AccessLimitMeta(@NotNull AccessLimit accessLimit, @NotNull Method method) {
        this.value = accessLimit.value();
        this.reveal = accessLimit.reveal();
        this.times = accessLimit.times();
        this.timeout = accessLimit.timeout();
        this.methodName = method.getName();
    }

    public String getTimes() {
        return times + "";
    }

    public String getTimeout() {
        return timeout + "";
    }


}
