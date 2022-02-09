package com.matrixboot.idempotent.core;

import com.matrixboot.idempotent.annotation.Idempotent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * create in 2022/1/24 4:04 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdempotentMeta {

    String value;

    String recover;

    String message;

    public IdempotentMeta(@NotNull Idempotent idempotent) {
        this.value = idempotent.value();
        this.recover = idempotent.recover();
        this.message = idempotent.message();
    }
}
