package com.c0ying.framework.exceldata;

public class HandlerWorkException extends RuntimeException {

    public HandlerWorkException() {
    }

    public HandlerWorkException(String message) {
        super(message);
    }

    public HandlerWorkException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerWorkException(Throwable cause) {
        super(cause);
    }
}
