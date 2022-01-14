package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.exception.AbstractAccessLimitException;

/**
 * <p>
 * create in 2021/12/16 11:14 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */

public class AccessLimitException extends AbstractAccessLimitException {

    private static final long serialVersionUID = 9025997025821912099L;

    public AccessLimitException(String message) {
        super(message);
    }
}
