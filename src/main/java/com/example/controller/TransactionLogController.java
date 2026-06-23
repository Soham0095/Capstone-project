package com.example.controller;

import com.example.dto.TransactionLogDto;
import com.example.service.TransactionLogService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionLogController {

    @Autowired
    private TransactionLogService transactionLogService;

    //get transaction logs by user id
    @GetMapping("/transactionLogs/{userId}")
    public List<TransactionLogDto> getTransactionLogsByUserId(@PathVariable Integer userId) {
        return transactionLogService.getTransactionLogsByUserId(userId);
    }

}
