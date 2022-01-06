package com.matrixboot.access.limit.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;

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

    Integer times;

    Integer timeout;

    TimeUnit unit;

}
