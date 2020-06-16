package com.zkkj.gps.gateway.ccs.exception;

/**
 * author : wn
 *  Date : 2020-06-08
 * 自定义时间有效行异常
 *
 */
public class TimeValidException extends RuntimeException {
    public TimeValidException() {
        super();
    }

    public TimeValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TimeValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeValidException(String message) {
        super(message);
    }

    public TimeValidException(Throwable cause) {
        super(cause);
    }
}
