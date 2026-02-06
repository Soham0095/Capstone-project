package com.example.service;

import com.example.dto.CreateAccountRequest;
import com.example.entity.Account;
import com.example.exception.AccountNotFoundException;

import java.util.Optional;

public interface AccountService {
    void createAccount(CreateAccountRequest request);

    Account getAccountById(int id) throws AccountNotFoundException;

    Integer getBalance(int id);
}
