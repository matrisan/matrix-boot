package com.matrixboot.idempotent.annotation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;

/**
 * 幂等注解的元数据
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

    /**
     * 支持 EL 表达式
     */
    String value;

    /**
     * 幂等的有效时间
     */
    Integer timeout;

    /**
     * 幂等的时间单位
     */
    TimeUnit unit;

}
