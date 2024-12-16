package com.suntravels.callcenter.advice;

import com.suntravels.callcenter.exception.NoContractsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Global exception handler to manage validation errors and illegal state exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException for validation failures.
     *
     * @param ex The exception containing the validation errors.
     * @return A map of field names and error messages with HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Iterate over all errors in the exception and extract the field name and error message
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            }
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles {@link NoContractsFoundException} thrown by the application.
     * <p>
     * This method is an exception handler for cases where no contracts are found.
     * It returns a `404 Not Found` status along with the exception's message as the response body.
     *
     * @param ex the exception instance containing details of the error
     * @return a {@link ResponseEntity} containing the exception message and the HTTP status code
     */
    @ExceptionHandler(NoContractsFoundException.class)
    public ResponseEntity<String> handleNoContractsFoundException(NoContractsFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


}
