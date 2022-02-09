package com.matrixboot.idempotent.exception;



/**
 * 幂等异常的父类
 * <p>
 * create in 2021/12/16 11:14 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */

public class GenerateTokenIdempotentException extends AbstractIdempotentException {

    private static final long serialVersionUID = -2655647751808440122L;

    public GenerateTokenIdempotentException(String message) {
        super(message);
    }
}
