package com.example.controller;

import com.example.dto.LoginResponseDto;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController {

    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;

    public LoginController(AccountRepository accountRepository, JwtUtil jwtUtil) {
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /login
     *
     * Validates username + password (plaintext comparison against DB).
     * On success, generates a signed JWT and returns it in the response body.
     * The client must store this token and send it as:
     *   Authorization: Bearer <token>
     * on every subsequent request.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<Account> accountOpt = accountRepository.findByUsername(username);

        if (accountOpt.isPresent() && password.equals(accountOpt.get().getPassword())) {
            Account account = accountOpt.get();

            // Generate JWT token
            String token = jwtUtil.generateToken(account.getUsername(), account.getId());

            LoginResponseDto response = new LoginResponseDto(
                    true,
                    "Login successful",
                    token,
                    new LoginResponseDto.AccountInfo(
                            account.getId(),
                            account.getHolderName(),
                            account.getUsername(),
                            account.getBalance(),
                            account.getStatus().name(),
                            account.getVersion()
                    )
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("ok", false, "message", "Invalid username or password")
        );
    }
}
