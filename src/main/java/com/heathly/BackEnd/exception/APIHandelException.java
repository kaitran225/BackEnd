package com.heathly.BackEnd.exception;

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
    public ResponseEntity handleBadRequestException (MethodArgumentNotValidException exception) {

        String messages ="";

        for(FieldError error: exception.getBindingResult().getFieldErrors()) {
            messages += error.getDefaultMessage() + "\n";
        }

        return new ResponseEntity(messages, HttpStatus.BAD_REQUEST);
    }
}
