package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository <Account, Integer>{
    @Query("SELECT a.balance from Account a where a.id = ?1")
    Optional<Integer> getBalance(Integer id);

    Optional<Account> findByUsername(String username);
}


