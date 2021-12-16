package com.matrixboot.idempotent.annotation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;

/**
 * TODO
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
public class IdempotentMeta {

    String value;

    Integer timeout;

    TimeUnit unit;

}
