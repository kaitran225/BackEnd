package com.healthy.BackEnd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// xử lí exception của api
// mỗi khi có lỗi validation thì chạy xử lí này
@RestControllerAdvice
public class APIHandelException {
    // mỗi khi có lỗi validation thì chạy xử lý này

    // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBadRequestException (MethodArgumentNotValidException exception) {

        StringBuilder messages = new StringBuilder();

        for(FieldError error: exception.getBindingResult().getFieldErrors()) {
            messages.append(error.getDefaultMessage()).append("\n");
        }

        return new ResponseEntity<>(messages.toString(), HttpStatus.BAD_REQUEST);
    }
}
