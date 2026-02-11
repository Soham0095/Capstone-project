package com.example.service;


import com.example.dto.CreateAccountRequest;
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
        account.setId(request.id());
        account.setHolderName(request.holderName());
        account.setUsername(request.username());
        account.setpassword(request.password());
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
