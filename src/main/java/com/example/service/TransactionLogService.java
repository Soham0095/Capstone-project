package com.example.service;

import com.example.dto.TransactionLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface TransactionLogService {
    public void createTransactionLog(TransactionLogDto transactionLogDto);

}
