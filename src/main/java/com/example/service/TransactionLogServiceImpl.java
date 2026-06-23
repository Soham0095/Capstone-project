package com.example.service;


import com.example.dto.TransactionLogDto;
import com.example.entity.Account;
import com.example.entity.TransactionLog;
import com.example.exception.AccountNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.TransactionLogRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionLogService")
public class TransactionLogServiceImpl implements TransactionLogService{

    @Autowired
    private TransactionLogRepository transactionLogRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    public void createTransactionLog(TransactionLog transactionLog){
        transactionLogRepository.save(transactionLog);
        System.out.println("Transaction Log saved successfully");
    }

    @Override
    public List<TransactionLogDto> getTransactionLogsByUserId(Integer userId) {
        try {
            accountService.getAccountById(userId);
        } catch (AccountNotFoundException e) {
            throw e;
        }

        List<TransactionLog> logs = transactionLogRepository.findByUserId(userId);

        return logs.stream().map(log -> {
            String fromName = accountRepository.findById(log.getFromAccountId())
                    .map(Account::getHolderName)
                    .orElse("Account #" + log.getFromAccountId());
            String toName = accountRepository.findById(log.getToAccountId())
                    .map(Account::getHolderName)
                    .orElse("Account #" + log.getToAccountId());

            return new TransactionLogDto(
                    log.getId(),
                    log.getFromAccountId(),
                    fromName,
                    log.getToAccountId(),
                    toName,
                    log.getAmount(),
                    log.getStatus(),
                    log.getFailureReason(),
                    log.getCreatedOn()
            );
        }).collect(Collectors.toList());
    }
}

