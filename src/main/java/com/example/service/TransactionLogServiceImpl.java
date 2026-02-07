package com.example.service;


import com.example.entity.TransactionLog;
import com.example.exception.AccountNotFoundException;
import com.example.repository.TransactionLogRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionLogService")
public class TransactionLogServiceImpl implements TransactionLogService{

    @Autowired
    private TransactionLogRepository transactionLogRepository;
    @Autowired
    private AccountService accountService;

    public void createTransactionLog(TransactionLog transactionLog){

        transactionLogRepository.save(transactionLog);

        System.out.println("Transaction Log saved successfully");
    }
    @Override
    public List<TransactionLog> getTransactionLogsByUserId(Integer userId) {

        // does user exist
        try{
            accountService.getAccountById(userId);
            return transactionLogRepository.findByUserId(userId);

        }
        catch(AccountNotFoundException e){
            throw e;
        }

    }
}
