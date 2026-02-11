package com.example.service;


import com.example.dto.CreateAccountRequest;
import com.example.dto.LoginRequestDto;
import com.example.dto.TransactionRequestDto;
import com.example.entity.Account;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientBalanceException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("accountService")
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;

    public void createAccount(CreateAccountRequest request){
        Account account = new Account();
//        account.setId(request.id());
        account.setHolderName(request.holderName());
        account.setUsername(request.username());
        account.setpassword(request.password());
        System.out.println(account);
        accountRepository.save(account);
    }

    public String Login(LoginRequestDto loginRequestDto) {
        // 1. Fetch account (Uses your existing AccountNotFoundException)
        Account account = (Account) accountRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(() -> new AccountNotFoundException("Account with username " + loginRequestDto.username() + " not found"));

        // 2. Validate password
        if (!account.getPassword().equals(loginRequestDto.password())) {
            // Option A: Throw a custom exception that your GlobalExceptionHandler catches
            // Option B: Return a string that the Controller interprets
            return "Invalid Credentials";
        }

        return "Login Successful";
    }

    public Account getAccountById(int id){
        return accountRepository.findById(id)
                                .orElseThrow(() -> new AccountNotFoundException("Account " + id + " not found"));
    }

    public Integer getBalance(int id){

        return accountRepository.getBalance(id)
                .orElseThrow(()-> new AccountNotFoundException("Account " + id + " not found"));
    }

        public void updateAccount(Account account){
            accountRepository.save(account);
        }


        @Transactional
        @Override
        public void updateBalance(TransactionRequestDto TransactionRequestDto) {

        Account account= accountRepository.findById(TransactionRequestDto.accountId())
                        .orElseThrow(() -> new AccountNotFoundException("Account " + TransactionRequestDto.accountId() + " not found"));

        if("withdraw".equalsIgnoreCase(TransactionRequestDto.action())){
            if(account.getBalance() < TransactionRequestDto.amount()){
                throw new InsufficientBalanceException("Insufficient balance");
            }
            account.setBalance(account.getBalance() - TransactionRequestDto.amount());
        } else if("deposit".equalsIgnoreCase(TransactionRequestDto.action())){
            account.setBalance(account.getBalance() + TransactionRequestDto.amount());
        } else {
            throw new IllegalArgumentException("Invalid action: " + TransactionRequestDto.action());
        }

        updateAccount(account);


    }
}
