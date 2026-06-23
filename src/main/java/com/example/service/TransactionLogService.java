package com.example.service;


import java.util.List;


import org.springframework.stereotype.Service;

import com.example.dto.TransactionLogDto;
import com.example.entity.TransactionLog;

@Service
public interface TransactionLogService {
    public void createTransactionLog(TransactionLog transactionLog);

    public List<TransactionLogDto> getTransactionLogsByUserId(Integer userId);

}
