package ru.heikkz.jp.exception;

public class MyEntityNotFoundException extends RuntimeException {

    public MyEntityNotFoundException() {
    }

    public MyEntityNotFoundException(String message) {
        super(message);
    }

    public MyEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyEntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
