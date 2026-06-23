package com.example.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.CreateAccountRequest;
import com.example.entity.Account;
import com.example.service.AccountService;

import java.util.Map;

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    // creating new account
    @PostMapping("/newAccount")
    public Map<String, Object> createAccount(
            @RequestBody CreateAccountRequest request){

        accountService.createAccount(request);

        return Map.of(
            "success", true,
            "message", "Account created for " + request.username()
        );
    }

    // getting account details by id
    @GetMapping("/accounts/{id}")
    public Account getAccountById(@PathVariable int id){
        return accountService.getAccountById(id);
    }

    // getting account balance by id
    @GetMapping("/accounts/getBalance/{id}")
    public Integer getAccountBalance(@PathVariable int id) {
        return accountService.getBalance(id);
    }

    // deposit or withdraw
    @PatchMapping("/accounts/updateBalance")
    public ResponseEntity<Map<String, Object>> updateBalance(@RequestBody Map<String, Object> request) {
        int accountId = (Integer) request.get("accountId");
        int amount    = (Integer) request.get("amount");
        String action = (String)  request.get("action");

        Account account = accountService.getAccountById(accountId);

        if ("DEPOSIT".equalsIgnoreCase(action)) {
            account.setBalance(account.getBalance() + amount);
        } else if ("WITHDRAW".equalsIgnoreCase(action)) {
            if (account.getBalance() < amount) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Insufficient balance"
                ));
            }
            account.setBalance(account.getBalance() - amount);
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid action: " + action
            ));
        }

        accountService.updateAccount(account);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", action + " successful",
            "newBalance", account.getBalance()
        ));
    }
}
