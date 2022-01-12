package com.matrixboot.idempotent.annotation;



/**
 * 幂等异常的父类
 * <p>
 * create in 2021/12/16 11:14 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */

public class IdempotentException extends RuntimeException {

    private static final long serialVersionUID = -2655647751808440122L;

    public IdempotentException(String message) {
        super(message);
    }
}
