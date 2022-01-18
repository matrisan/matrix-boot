package com.matrixboot.access.limit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO
 * <p>
 * create in 2022/1/18 3:24 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
public class AccessLimitResult {

    private Boolean result;

    private String message;

    private AccessLimitMeta meta;

    public AccessLimitResult(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }
}
