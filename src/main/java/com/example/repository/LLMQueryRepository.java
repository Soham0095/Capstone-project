package com.example.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class LLMQueryRepository{

    private final JdbcTemplate jdbcTemplate;

    public LLMQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeSQLQuery(String sqlQuery) {
        return jdbcTemplate.queryForList(sqlQuery);
    }

}
