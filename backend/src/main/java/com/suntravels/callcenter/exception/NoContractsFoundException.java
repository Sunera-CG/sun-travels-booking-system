package com.suntravels.callcenter.exception;

public class NoContractsFoundException extends RuntimeException {

    private final String message;


    /**
     * Constructs a new {@code NoContractsFoundException} with the specified error message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public NoContractsFoundException(String message) {
        super(message);
        this.message = message;
    }


}
