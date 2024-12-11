package com.suntravels.callcenter.exception;
import lombok.Getter;

@Getter
public class InvalidIdException extends RuntimeException {

    private final String message;

    public InvalidIdException(String message) {
        super(message);
        this.message = message;
    }

}
