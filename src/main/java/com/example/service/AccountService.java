package com.example.service;

import com.example.dto.CreateAccountRequest;
import com.example.dto.LoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.TransactionRequestDto;
import com.example.entity.Account;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountNotFoundException;
import org.springframework.transaction.annotation.Transactional;


public interface AccountService {
    void createAccount(CreateAccountRequest request) throws AccountAlreadyExistsException;

    Account getAccountById(int id) throws AccountNotFoundException;

    Integer getBalance(int id);

    void updateAccount(Account account);

    void updateBalance(TransactionRequestDto TransactionRequestDto);

    LoginResponseDto Login(LoginRequestDto loginRequestDto);


}