package com.matrixboot.access.limit.exception;

/**
 * TODO
 * <p>
 * create in 2022/1/14 4:54 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public abstract class AbstractAccessLimitException extends RuntimeException {

    private static final long serialVersionUID = -487109143209292995L;

    public AbstractAccessLimitException(String message) {
        super(message);
    }
}
