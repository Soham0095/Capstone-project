package com.example.controller;

import com.example.dto.LoginResponseDto;
import com.example.exception.AccountNotActiveException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientBalanceException;
import com.example.exception.AccountAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccountNotActiveException.class,
            AccountNotFoundException.class,
            InsufficientBalanceException.class,
            AccountAlreadyExistsException.class

    })
    public ResponseEntity<LoginResponseDto> handleCustomExceptions(RuntimeException ex) {
        LoginResponseDto errorBody = new LoginResponseDto(ex.getMessage(), false, null, null);
        if (ex instanceof AccountNotFoundException){
            return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
        }
        else if (ex instanceof AccountNotActiveException) {
            return new ResponseEntity<>(errorBody, HttpStatus.FORBIDDEN);
        }
         else if (ex instanceof AccountAlreadyExistsException){
            return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
        }
        else{
            return new ResponseEntity<>(errorBody, HttpStatus.PAYMENT_REQUIRED);
        }

    }
}