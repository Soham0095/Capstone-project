package com.example.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ChatService {

    public List<Map<String, Object>> getChatResponse(String userPrompt);

    public String executeLLMQuery(String userPrompt);

    public List<Map<String, Object>> executeSQLQuery(String sqlQuery);


    
}
