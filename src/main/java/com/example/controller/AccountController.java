package com.example.controller;
import com.example.dto.LoginRequestDto;
import com.example.dto.LoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.CreateAccountRequest;
import com.example.entity.Account;
import com.example.service.AccountService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController{


    @Autowired
    private AccountService accountService;

    // creating new account
    @PostMapping("/newAccount")
    public ResponseEntity<Map<String, Object>> createAccount(
            @RequestBody CreateAccountRequest request){

        accountService.createAccount(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account created successfully");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }


    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto result = accountService.Login(loginRequestDto);

        if (result!=null && result.ok()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // Returning Unauthorized for failed match
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }
    // getting account details by id
    @GetMapping("/accounts/{id}")
    public Account getAccountById(@PathVariable int id){
        return accountService.getAccountById(id);
    }
    //handled exception

    // getting account balance by id
    @GetMapping("/accounts/getBalance/{id}")
    public Integer getAccountBalance(@PathVariable int id) {
        return accountService.getBalance(id);
    }
    //handled exception


    // updating account balance(withdraw or deposit)
    @PatchMapping("/accounts/updateBalance")
    public ResponseEntity<String> updateAccountBalance(@RequestBody com.example.dto.TransactionRequestDto transactionRequestDto) {

        accountService.updateBalance(transactionRequestDto);
        return new ResponseEntity<>("Account updated successfully", HttpStatus.OK);
    }
}

// all exception handling tested

