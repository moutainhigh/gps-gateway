package com.zkkj.gps.gateway.ccs.exception;

/**
 * author : cyc
 * Date : 2019-06-15
 * 自定义参数异常
 */
public class ParamException extends RuntimeException {

    public ParamException() {
        super();
    }

    public ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }


}
