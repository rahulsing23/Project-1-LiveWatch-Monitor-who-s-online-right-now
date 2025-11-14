package com.livewatch.exception;

public class InvalidSessionIdException extends IllegalArgumentException{
    public InvalidSessionIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSessionIdException(String message) {
        super(message);
    }
}
