package com.example.dto;

/**
 * Response body returned by POST /login on successful authentication.
 * The JWT token should be stored by the client and sent as:
 *   Authorization: Bearer <token>
 * on every subsequent request.
 */
public class LoginResponseDto {

    private boolean ok;
    private String message;
    private String token;
    private AccountInfo body;

    public LoginResponseDto(boolean ok, String message, String token, AccountInfo body) {
        this.ok = ok;
        this.message = message;
        this.token = token;
        this.body = body;
    }

    // --- Getters ---

    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public AccountInfo getBody() { return body; }

    // --- Nested account info DTO ---

    public static class AccountInfo {
        private Integer id;
        private String holderName;
        private String username;
        private Integer balance;
        private String status;
        private Long version;

        public AccountInfo(Integer id, String holderName, String username,
                           Integer balance, String status, Long version) {
            this.id = id;
            this.holderName = holderName;
            this.username = username;
            this.balance = balance;
            this.status = status;
            this.version = version;
        }

        public Integer getId() { return id; }
        public String getHolderName() { return holderName; }
        public String getUsername() { return username; }
        public Integer getBalance() { return balance; }
        public String getStatus() { return status; }
        public Long getVersion() { return version; }
    }
}
