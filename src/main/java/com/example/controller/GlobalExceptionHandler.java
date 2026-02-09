package com.example.controller;

import com.example.exception.AccountNotActiveException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientBalanceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler({
        AccountNotActiveException.class,
        AccountNotFoundException.class,
        InsufficientBalanceException.class

})
    public String handleCustomExceptions(RuntimeException ex) {
        return ex.getMessage();
    }


}
