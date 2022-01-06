package com.matrixboot.access.limit.config;

/**
 * <p>
 * create in 2021/12/16 11:14 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */

public class AccessLimitException extends RuntimeException{

    public AccessLimitException(String message) {
        super(message);
    }
}
