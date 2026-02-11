package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.example.repository.LLMQueryRepository;

@Service("chatService")
public class ChatServiceImpl implements ChatService {

    @Autowired
    private LLMQueryRepository llmQueryRepository;

    final ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String executeLLMQuery(String userPrompt) {

        String systemPrompt = """
                                    You are an SQL generator.

                                    You have access to a database with tables:
                                    account(id, holder_name, balance, status, version, last_updated)
                                    transaction_log(id, from_account_id, to_account_id, amount, status, failure_reason, idempotency_key, created_on)

                                    Rules (MANDATORY):
                                    - Return ONLY a valid SQL SELECT statement
                                    - Do NOT include explanations, reasoning, thoughts, or analysis
                                    - Do NOT include tags like <think>, <analysis>, or similar
                                    - Do NOT include markdown, comments, or extra text
                                    - Output must start with SELECT
                                    - Output must end with a semicolon

                                    Think silently.
                                    Do not reveal reasoning.
                                    Output only the final SQL SELECT statement.
                     """;


        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

        //String llmResponse = "SELECT id, holder_name FROM account WHERE id NOT IN (SELECT from_account_id FROM transaction_log);";
        // return llmResponse;

    }

    @Override
    public List<Map<String, Object>> executeSQLQuery(String sqlQuery) {
        return  llmQueryRepository.executeSQLQuery(sqlQuery);


    }

    @Override
    public List<Map<String, Object>> getChatResponse(String userPrompt) {
        String sqlQuery = executeLLMQuery(userPrompt);
        return executeSQLQuery(sqlQuery);
    }






}
