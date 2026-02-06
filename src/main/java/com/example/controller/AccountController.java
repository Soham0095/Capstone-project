package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.CreateAccountRequest;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @PostMapping("/newAccount")
    public ResponseEntity<String> createAccount(
            @RequestBody CreateAccountRequest request){

        // using service class
        accountService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account created for " + request.id());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable int id){
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/getBalance/{id}")

    public ResponseEntity<String> getAccountBalance(@PathVariable int id) {
        return accountService.getBalance(id)
                .map(b -> ResponseEntity.ok(String.valueOf(b)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Account Number " + id +  " not found"));
    }
}


