package com.example.controller;

import com.example.dto.TransferRequestDto;
import com.example.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TransferController {
   @Autowired
   private TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody TransferRequestDto transferRequestDto){
        System.out.println(transferRequestDto);
        transferService.transfer(transferRequestDto);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Transfer of ₹" + transferRequestDto.amount() + " completed successfully"
        ));
    }
}
