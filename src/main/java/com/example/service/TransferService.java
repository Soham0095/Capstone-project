package com.example.service;

import com.example.dto.TransferRequestDto;
import com.example.exception.AccountNotActiveException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientBalanceException;

public interface TransferService {

    public boolean isValidTransfer(TransferRequestDto transferRequestDto) throws
            AccountNotFoundException, AccountNotActiveException, InsufficientBalanceException;
    public void transfer(TransferRequestDto transferRequestDto);
}
