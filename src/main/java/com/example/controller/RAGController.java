package com.example.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.example.service.ChatService;

@RestController
public class RAGController{

    @Autowired
    private ChatService chatService;

    // creating new account
    @PostMapping("/chat")
    public ResponseEntity<List<Map<String, Object>>> getChatResponse(
            @RequestBody  String userPrompt){

        List<Map<String, Object>> llmResponse = chatService.getChatResponse(userPrompt);
        return ResponseEntity.ok(llmResponse);
    }

}