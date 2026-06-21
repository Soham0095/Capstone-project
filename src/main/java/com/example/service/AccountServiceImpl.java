package com.example.service;


import com.example.dto.CreateAccountRequest;
import com.example.dto.LoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.TransactionRequestDto;
import com.example.entity.Account;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientBalanceException;
import com.example.repository.AccountRepository;
import com.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("accountService")
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void createAccount(CreateAccountRequest request){
        //handle duplicate username
        if (accountRepository.findByUsername(request.username()).isPresent()) {
            throw new AccountAlreadyExistsException("Account with username " + request.username() + " already exists");
        }
        Account account = new Account();
//        account.setId(request.id());
        account.setHolderName(request.holderName());
        account.setUsername(request.username());
        account.setpassword(passwordEncoder.encode(request.password()));
        System.out.println(account);
        accountRepository.save(account);
    }

    public LoginResponseDto Login(LoginRequestDto loginRequestDto) {
        // 1. Fetch account (Uses your existing AccountNotFoundException)
        Account account = (Account) accountRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(() -> new AccountNotFoundException("Account with username " + loginRequestDto.username() + " not found"));
        String message;
        boolean ok = true;
        String token = null;
        // 2. Validate password
        boolean passwordMatches = passwordEncoder.matches(loginRequestDto.password(), account.getPassword());
        
        // Fallback for plain text passwords (migration support for existing accounts)
        if (!passwordMatches && !account.getPassword().startsWith("$2a$") && !account.getPassword().startsWith("$2b$")) {
            passwordMatches = loginRequestDto.password().equals(account.getPassword());
            if (passwordMatches) {
                // Encode and save the password for future logins
                account.setpassword(passwordEncoder.encode(loginRequestDto.password()));
                accountRepository.save(account);
            }
        }
        
        if (!passwordMatches) {
            message = "Invalid Credentials";
            ok = false;
        }
        else {
            message = "Login Successful";
            token = jwtUtil.generateToken(account.getUsername(), account.getId());
        }
        //masking password
        account.setpassword("");
        return new LoginResponseDto(message, ok, account, token);
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