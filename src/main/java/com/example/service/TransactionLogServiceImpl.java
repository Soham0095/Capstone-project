package com.example.service;

import com.example.dto.TransactionLogDto;
import com.example.entity.TransactionLog;
import com.example.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionLogService")
public class TransactionLogServiceImpl implements TransactionLogService{

    @Autowired
    private TransactionLogRepository transactionLogRepository;
    public void createTransactionLog(TransactionLogDto transactionLogDto){
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setFromAccountId(transactionLogDto.fromAccountId());
        transactionLog.setToAccountId(transactionLogDto.toAccountId());
        transactionLog.setAmount(transactionLogDto.fromAccountId());

        transactionLogRepository.save(transactionLog);

        System.out.println("Transaction Log saved successfully");
    }
}
