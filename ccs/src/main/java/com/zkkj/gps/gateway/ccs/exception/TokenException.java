package com.zkkj.gps.gateway.ccs.exception;

/**
 * author : cyc
 * Date : 2020/5/12
 * token令牌异常
 */
public class TokenException extends RuntimeException {

    public TokenException() {
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    public TokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
