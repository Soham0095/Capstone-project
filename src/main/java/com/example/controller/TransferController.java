package com.example.controller;

import com.example.dto.TransferRequestDto;
import com.example.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {
   @Autowired
   private TransferService transferService;
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequestDto transferRequestDto){
        // transferService should take it up
        System.out.println(transferRequestDto);
        transferService.transfer(transferRequestDto);
        return "Valid Transfer";
    }
}
//exce handling tested
