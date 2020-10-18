package ru.heikkz.jp.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiError {

    private final int status;
    private final String message;
    private final String developerMessage;

    public ApiError(int status, String message, String developerMessage) {
        this.status = status;
        this.message = message;
        this.developerMessage = developerMessage;
    }
}
