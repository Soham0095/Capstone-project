package com.example.service;

import com.example.dto.CreateAccountRequest;
import com.example.dto.TransferRequestDto;
import com.example.entity.Account;
import com.example.entity.RewardLog;
import com.example.repository.AccountRepository;
import com.example.repository.RewardLogRepository;
import com.example.exception.InsufficientBalanceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransferServiceRewardTests {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RewardLogRepository rewardLogRepository;

    private Account user1AccountA;
    private Account user1AccountB;
    private Account user2Account;

    @BeforeEach
    public void setUp() {
        rewardLogRepository.deleteAll();
        
        // Clean existing test accounts if left over
        accountRepository.findByUsername("user1-testa").ifPresent(o -> accountRepository.delete((Account) o));
        accountRepository.findByUsername("user2-test").ifPresent(o -> accountRepository.delete((Account) o));

        // Create Account for User 1 (Account A)
        CreateAccountRequest req1 = new CreateAccountRequest(null, "User One A", "user1-testa", "pass1");
        accountService.createAccount(req1);
        user1AccountA = (Account) accountRepository.findByUsername("user1-testa").get();
        user1AccountA.setBalance(1000);
        accountService.updateAccount(user1AccountA);

        // Create Account for User 2
        CreateAccountRequest req2 = new CreateAccountRequest(null, "User Two", "user2-test", "pass2");
        accountService.createAccount(req2);
        user2Account = (Account) accountRepository.findByUsername("user2-test").get();
        user2Account.setBalance(500);
        accountService.updateAccount(user2Account);

        // Create Account for User 1 (Account B - same username/user, different account ID)
        user1AccountB = new Account();
        user1AccountB.setHolderName("User One B");
        user1AccountB.setUsername("user1-testa"); // same username as Account A
        user1AccountB.setpassword("pass1");
        user1AccountB.setBalance(300);
        accountRepository.save(user1AccountB);
    }

    @AfterEach
    public void tearDown() {
        try {
            rewardLogRepository.deleteAll();
            if (user1AccountB != null && user1AccountB.getId() != null) {
                accountRepository.delete(user1AccountB);
            }
            if (user1AccountA != null && user1AccountA.getId() != null) {
                accountRepository.delete(user1AccountA);
            }
            if (user2Account != null && user2Account.getId() != null) {
                accountRepository.delete(user2Account);
            }
        } catch (Exception e) {
            // Ignore teardown cleanup errors if any
        }
    }

    @Test
    public void testTransferUnderLimit_NoRewards() {
        TransferRequestDto dto = new TransferRequestDto(user1AccountA.getId(), user2Account.getId(), 99);
        transferService.transfer(dto);

        Account fromAcc = accountService.getAccountById(user1AccountA.getId());
        Account toAcc = accountService.getAccountById(user2Account.getId());
        assertEquals(901, fromAcc.getBalance());
        assertEquals(599, toAcc.getBalance());
        assertEquals(0, fromAcc.getRewardPoints() == null ? 0 : fromAcc.getRewardPoints());

        List<RewardLog> history = rewardLogRepository.findByAccountIdOrderByCreatedOnDesc(fromAcc.getId());
        assertTrue(history.isEmpty());
    }

    @Test
    public void testTransferEligible_EarnsRewards() {
        TransferRequestDto dto = new TransferRequestDto(user1AccountA.getId(), user2Account.getId(), 250);
        transferService.transfer(dto);

        Account fromAcc = accountService.getAccountById(user1AccountA.getId());
        Account toAcc = accountService.getAccountById(user2Account.getId());
        assertEquals(750, fromAcc.getBalance());
        assertEquals(750, toAcc.getBalance());
        assertEquals(2, fromAcc.getRewardPoints());

        List<RewardLog> history = rewardLogRepository.findByAccountIdOrderByCreatedOnDesc(fromAcc.getId());
        assertEquals(1, history.size());
        RewardLog log = history.get(0);
        assertEquals(2, log.getPoints());
        assertTrue(log.getDescription().contains("Earned 2 reward points"));
        assertTrue(log.getDescription().contains("Rs. 250"));
    }

    @Test
    public void testSelfTransfer_NoRewards() {
        TransferRequestDto dto = new TransferRequestDto(user1AccountA.getId(), user1AccountA.getId(), 250);
        transferService.transfer(dto);

        Account fromAcc = accountService.getAccountById(user1AccountA.getId());
        assertEquals(1000, fromAcc.getBalance());
        assertEquals(0, fromAcc.getRewardPoints() == null ? 0 : fromAcc.getRewardPoints());

        List<RewardLog> history = rewardLogRepository.findByAccountIdOrderByCreatedOnDesc(fromAcc.getId());
        assertTrue(history.isEmpty());
    }

    @Test
    public void testSameUserDifferentAccounts_NoRewards() {
        // Transfer 250 from Account A to Account B (same username user1-testa)
        TransferRequestDto dto = new TransferRequestDto(user1AccountA.getId(), user1AccountB.getId(), 250);
        transferService.transfer(dto);

        Account fromAcc = accountService.getAccountById(user1AccountA.getId());
        Account toAcc = accountService.getAccountById(user1AccountB.getId());
        assertEquals(750, fromAcc.getBalance());
        assertEquals(550, toAcc.getBalance());
        
        // Since sender and receiver are same user, no rewards should be earned
        assertEquals(0, fromAcc.getRewardPoints() == null ? 0 : fromAcc.getRewardPoints());

        List<RewardLog> history = rewardLogRepository.findByAccountIdOrderByCreatedOnDesc(fromAcc.getId());
        assertTrue(history.isEmpty());
    }

    @Test
    public void testFailedTransfer_NoRewards() {
        TransferRequestDto dto = new TransferRequestDto(user1AccountA.getId(), user2Account.getId(), 1500);

        assertThrows(InsufficientBalanceException.class, () -> {
            transferService.transfer(dto);
        });

        Account fromAcc = accountService.getAccountById(user1AccountA.getId());
        assertEquals(1000, fromAcc.getBalance());
        assertEquals(0, fromAcc.getRewardPoints() == null ? 0 : fromAcc.getRewardPoints());

        List<RewardLog> history = rewardLogRepository.findByAccountIdOrderByCreatedOnDesc(fromAcc.getId());
        assertTrue(history.isEmpty());
    }
}
