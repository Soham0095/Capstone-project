package com.example.controller;

import com.example.dto.TransactionLogDto;
import com.example.entity.TransactionLog;
import com.example.enums.TransactionStatus;
import com.example.repository.TransactionLogRepository;
import com.example.service.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionLogController {

    @Autowired
    private TransactionLogService transactionLogService;

}
