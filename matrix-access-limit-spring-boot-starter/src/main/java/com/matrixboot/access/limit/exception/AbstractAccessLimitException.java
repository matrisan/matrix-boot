package com.matrixboot.access.limit.exception;

/**
 * 请求速率的父类异常
 * <p>
 * create in 2022/1/14 4:54 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public abstract class AbstractAccessLimitException extends RuntimeException {

    private static final long serialVersionUID = -487109143209292995L;

    protected AbstractAccessLimitException(String message) {
        super(message);
    }
}
