package com.example.service;


import com.example.dto.CreateAccountRequest;
import com.example.entity.Account;
import com.example.exception.AccountNotFoundException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("accountService")
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;

    public void createAccount(CreateAccountRequest request){
        Account account = new Account();
        account.setId(request.id());
        account.setHolderName(request.holderName());
        System.out.println(account);
        accountRepository.save(account);
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
}
