package com.matrixboot.access.limit.dto;

import com.matrixboot.access.limit.annotation.AccessLimit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

import static lombok.AccessLevel.PRIVATE;

/**
 * 这个类创建了以后不允许修改除非重新生成
 *
 * <p>
 * create in 2021/12/16 11:17 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Getter
@Setter(PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public final class AccessLimitMeta {

    String value;

    String recover;

    Integer times;

    Integer timeout;

    String methodName;

    String message;

    public AccessLimitMeta(@NotNull AccessLimit accessLimit) {
        this.value = accessLimit.value();
        this.recover = accessLimit.recover();
        this.times = accessLimit.times();
        this.timeout = accessLimit.timeout();
        this.message = accessLimit.message();
    }


    public AccessLimitMeta(@NotNull AccessLimit accessLimit, @NotNull Method method) {
        this.value = accessLimit.value();
        this.recover = accessLimit.recover();
        this.times = accessLimit.times();
        this.timeout = accessLimit.timeout();
        this.methodName = method.getName();
        this.message = accessLimit.message();
    }

    @Contract(pure = true)
    public @NotNull String getTimes() {
        return times + "";
    }

    @Contract(pure = true)
    public @NotNull String getTimeout() {
        return timeout + "";
    }


}
