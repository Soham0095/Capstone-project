package com.example.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.dto.CreateAccountRequest;
import com.example.entity.Account;
import com.example.service.AccountService;

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @PostMapping("/newAccount")
    public String createAccount(
            @RequestBody CreateAccountRequest request){

        // using service class
        accountService.createAccount(request);

        return "Account created for " + request.id();
    }

    @GetMapping("/accounts/{id}")
    public Account getAccountById(@PathVariable int id){
        return accountService.getAccountById(id);
    }

    @GetMapping("/accounts/getBalance/{id}")
    public Integer getAccountBalance(@PathVariable int id) {
        return accountService.getBalance(id);
    }
}


