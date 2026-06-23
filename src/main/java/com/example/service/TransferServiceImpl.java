package com.example.service;

import com.example.dto.TransferRequestDto;
import com.example.entity.Account;
import com.example.entity.TransactionLog;
import com.example.enums.AccountStatus;
import com.example.enums.TransactionStatus;
import com.example.exception.AccountNotActiveException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("transferService")
public class TransferServiceImpl implements TransferService{

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionLogService transactionLogService;

    /*
    checks if the transfer request is valid. 
    Throws RuntimExceptions: AccountNotFoundException, AccountNotActiveException, InsufficientBalanceException
    */
    @Override
    public boolean isValidTransfer(TransferRequestDto transferRequestDto){

            // validate account IDs are provided
            if (transferRequestDto.fromAccountId() == null) {
                throw new AccountNotFoundException("Source account ID is required");
            }
            if (transferRequestDto.toAccountId() == null) {
                throw new AccountNotFoundException("Destination account ID is required");
            }

            // validate amount
            if (transferRequestDto.amount() == null || transferRequestDto.amount() <= 0) {
                throw new IllegalArgumentException("Transfer amount must be greater than zero");
            }

            // prevent self-transfer
            if (transferRequestDto.fromAccountId().equals(transferRequestDto.toAccountId())) {
                throw new IllegalArgumentException("Cannot transfer to the same account");
            }

            // does account exist? (throws AccountNotFoundException if not found)
            Account fromAccount = accountService.getAccountById(transferRequestDto.fromAccountId());
            Account toAccount = accountService.getAccountById(transferRequestDto.toAccountId());

            // status active?
            if (fromAccount.getStatus() != AccountStatus.active) {
                throw new AccountNotActiveException("Source account " + transferRequestDto.fromAccountId() + " is not active");
            }
            if (toAccount.getStatus() != AccountStatus.active) {
                throw new AccountNotActiveException("Destination account " + transferRequestDto.toAccountId() + " is not active");
            }

            // sufficient balance?
            if (fromAccount.getBalance() < transferRequestDto.amount()){
                throw new InsufficientBalanceException("Insufficient balance: account " + transferRequestDto.fromAccountId()
                    + " has ₹" + fromAccount.getBalance() + " but transfer requires ₹" + transferRequestDto.amount());
            }

        return true;
    }

    // executes the transfer without any validation or logging.
    @Transactional
    @Override
    public void executeTransfer(TransferRequestDto transferRequestDto){
        Account fromAccount = accountService.getAccountById(transferRequestDto.fromAccountId());
        Account toAccount = accountService.getAccountById(transferRequestDto.toAccountId());
        fromAccount.setBalance(fromAccount.getBalance() - transferRequestDto.amount());
        toAccount.setBalance(toAccount.getBalance() + transferRequestDto.amount());
        accountService.updateAccount(fromAccount);
        accountService.updateAccount(toAccount);

    }

    // main transfer func - AOP
    @Override
    public void transfer(TransferRequestDto transferRequestDto) {


        isValidTransfer(transferRequestDto);
        executeTransfer(transferRequestDto);
    }

}

