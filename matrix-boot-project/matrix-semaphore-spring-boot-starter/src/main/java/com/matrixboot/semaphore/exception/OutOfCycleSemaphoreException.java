package com.matrixboot.semaphore.exception;

import lombok.Getter;

/**
 * <p>
 * create in 2022/1/14 8:59 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class OutOfCycleSemaphoreException extends AbstractSemaphoreException {

    private static final long serialVersionUID = -66555424143691798L;

    @Getter
    private final String message;

    public OutOfCycleSemaphoreException(String message) {
        this.message = message;
    }
}
