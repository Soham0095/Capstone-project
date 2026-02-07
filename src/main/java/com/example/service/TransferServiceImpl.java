package com.example.service;

import com.example.dto.TransferRequestDto;
import com.example.entity.Account;
import com.example.entity.TransactionLog;
import com.example.enums.AccountStatus;
import com.example.enums.TransactionStatus;
import com.example.exception.AccountNotActiveException;
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

            // does account exist?
            Account fromAccount = accountService.getAccountById(transferRequestDto.fromAccountId());
            Account toAccount = accountService.getAccountById(transferRequestDto.toAccountId());

            // status active?
            if (!(fromAccount.getStatus() == AccountStatus.active && toAccount.getStatus() == AccountStatus.active)){
                throw new AccountNotActiveException("Account not active");
            }

            // sufficient balance?
        if (fromAccount.getBalance() < transferRequestDto.amount()){
            throw new InsufficientBalanceException("Account " + transferRequestDto.toAccountId() + " has insufficient balance");
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
        // create a transaction log - AOP
        TransactionLog transactionLog = new TransactionLog(
                transferRequestDto.fromAccountId(),
                transferRequestDto.toAccountId(),
                transferRequestDto.amount()
        );
        try {
            // validate transfer - AOP
            isValidTransfer(transferRequestDto);
            //execute func
            executeTransfer(transferRequestDto);
           // update transaction log - AOP
            transactionLog.setStatus(TransactionStatus.SUCCESS);

            // exception handling and logging - AOP
        } catch (Exception e){
            transactionLog.setStatus(TransactionStatus.FAILURE);
            transactionLog.setFailureReason(e.getMessage());
            throw e;
        }
        finally {
            // save transaction log - AOP
            transactionLogService.createTransactionLog(transactionLog);
        }
    }
}
