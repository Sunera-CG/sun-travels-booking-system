package com.suntravels.callcenter.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to manage validation errors and illegal state exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException for validation failures.
     * @param ex The exception containing the validation errors.
     * @return A map of field names and error messages with HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex ){

        Map<String,String> errors = new HashMap<>();

        // Iterate over all errors in the exception and extract the field name and error message
        ex.getBindingResult().getAllErrors().forEach((error)->{
            if(error instanceof FieldError){
                String fieldName = ((FieldError)error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName,errorMessage);
            }
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalStateException and returns the error message.
     * @param ex The IllegalStateException containing the error message.
     * @return The exception message with HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIlleagalStateException(IllegalStateException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
