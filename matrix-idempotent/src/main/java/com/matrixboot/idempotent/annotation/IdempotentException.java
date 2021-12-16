package com.matrixboot.idempotent.annotation;

/**
 * TODO
 * <p>
 * create in 2021/12/16 11:14 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */

public class IdempotentException extends RuntimeException{

    public IdempotentException(String message) {
        super(message);
    }
}
