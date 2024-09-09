package com.xcoder.ecommerce.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.HashMap;
import java.util.Map;

import com.xcoder.ecommerce.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(BAD_REQUEST).body(e.getMsg());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors()
            .forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String defaultMessage = error.getDefaultMessage();
                errors.put(fieldName, defaultMessage);
            });

        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(errors));
    }
}
