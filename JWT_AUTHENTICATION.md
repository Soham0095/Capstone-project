# JWT Authentication — Implementation Guide

## Overview

This document explains the **JWT (JSON Web Token) authentication** added to the Capstone Fund Transfer application. It covers what JWT is, why it was added, every file that was created or modified, and how the token flows through the entire application.

---

## What is JWT and Why We Added It

Before JWT, the `/login` endpoint simply verified the username and password and returned account details. There was **no way for the backend to verify who was making subsequent requests** (transfers, balance checks, etc.). Anyone who knew the API URL could call it directly.

**With JWT:**
- The server issues a **signed token** on login
- Every subsequent API request must carry that token
- The server validates the token before executing any operation
- Requests without a valid token get a **401 Unauthorized** response automatically

JWT is **stateless** — the server stores no session data. The token itself contains all the information needed (username, account ID, expiry), signed with a secret key that only the server knows.

---

## Token Structure

A JWT looks like this:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiYWNjb3VudElkIjoxfQ.Xk2F...
       │                          │                                 │
   Header (base64)          Payload (base64)              Signature (HMAC-SHA256)
```

Decoded payload contains:
```json
{
  "sub": "john",         ← username
  "accountId": 1,        ← account ID
  "iat": 1753000000,     ← issued at (Unix timestamp)
  "exp": 1753086400      ← expires at (24 hours later)
}
```

The **signature** is generated using a secret key stored only on the server. If anyone tampers with the payload, the signature breaks and the token is rejected.

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                    ANGULAR FRONTEND                          │
│                                                              │
│  login form ──► POST /login ──► receives token               │
│                                      │                       │
│                              localStorage['auth_token']      │
│                                      │                       │
│              jwt.interceptor.ts ◄────┘                       │
│              (runs on every HTTP call)                       │
│                      │                                       │
│              adds: Authorization: Bearer eyJ...              │
│                      │                                       │
│  auth-guard.ts ──► checks token exists before navigation     │
└──────────────────────────────┬───────────────────────────────┘
                               │ HTTP request with Bearer token
                               ▼
┌──────────────────────────────────────────────────────────────┐
│                   SPRING BOOT BACKEND                        │
│                                                              │
│  JwtAuthFilter ──► reads Authorization header                │
│       │            validates token (JwtUtil)                 │
│       │            sets SecurityContext                       │
│       ▼                                                      │
│  SecurityConfig ──► /login, /newAccount → public             │
│                     all other routes  → require valid JWT    │
│                                                              │
│  LoginController ──► on success: calls JwtUtil.generateToken │
│                      returns token in response body          │
└──────────────────────────────────────────────────────────────┘
```

---

## Files Changed or Created

### BACKEND — Spring Boot

---

#### 1. `pom.xml` — Added Dependencies

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JJWT (Java JWT library) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

**Why:** Spring Security provides the filter chain and security infrastructure. JJWT is the library used to create, sign, and parse JWT tokens.

---

#### 2. `application.yaml` — JWT Configuration

```yaml
jwt:
  secret: 7A25432A462D4A614E645267556B58703273357638792F423F4528482B4D6251
  expiration-ms: 86400000   # 24 hours in milliseconds
```

**Why:** The secret is a 256-bit hex key used to sign tokens with HMAC-SHA256. The expiry defines how long a token stays valid (24 hours). These are read by `JwtUtil` via `@Value`.

---

#### 3. `src/main/java/com/example/security/JwtUtil.java` — NEW FILE

**Role:** The core token engine. Responsible for:
- **Generating** a signed JWT on login
- **Validating** a token (checks signature and expiry)
- **Extracting** the username from a token

```java
// Generate token with username + accountId claims
public String generateToken(String username, Integer accountId) {
    return Jwts.builder()
            .subject(username)
            .claim("accountId", accountId)
            .issuedAt(new Date(now))
            .expiration(new Date(now + expirationMs))
            .signWith(key)   // signs with HMAC-SHA256
            .compact();
}

// Validate — returns false if signature is wrong or token expired
public boolean validateToken(String token) { ... }

// Extract the username (sub claim) from a valid token
public String extractUsername(String token) { ... }
```

---

#### 4. `src/main/java/com/example/security/CustomUserDetailsService.java` — NEW FILE

**Role:** Bridges Spring Security with our `AccountRepository`. Spring Security needs a `UserDetailsService` to load a user by username when validating a token.

```java
@Override
public UserDetails loadUserByUsername(String username) {
    Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(...));
    return new User(account.getUsername(), account.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
}
```

**Why:** After the JWT filter extracts the username from the token, it calls this service to load the full user details and build the Spring Security `Authentication` object.

---

#### 5. `src/main/java/com/example/security/JwtAuthFilter.java` — NEW FILE

**Role:** Runs on **every single HTTP request** before it reaches any controller. It:
1. Reads the `Authorization: Bearer <token>` header
2. Validates the token using `JwtUtil`
3. Loads the user using `CustomUserDetailsService`
4. Sets the authenticated user into `SecurityContextHolder`

```java
@Override
protected void doFilterInternal(HttpServletRequest request, ...) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Mark request as authenticated in Spring Security
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
            );
        }
    }
    filterChain.doFilter(request, response); // continue to controller
}
```

**Why:** This is the gatekeeper. Every protected endpoint passes through this filter.

---

#### 6. `src/main/java/com/example/config/SecurityConfig.java` — NEW FILE

**Role:** Configures the Spring Security filter chain — defines which routes are public and which require a JWT.

```java
http
    .csrf(AbstractHttpConfigurer::disable)          // REST API — no CSRF needed
    .cors(cors -> {})                               // use CorsConfig settings
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/newAccount").permitAll()  // public
        .anyRequest().authenticated()               // everything else needs JWT
    )
    .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // no sessions
    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
```

Key decisions:
- **CSRF disabled** — JWT is stateless, no cookies, so CSRF attacks don't apply
- **STATELESS session** — no server-side session is created or maintained
- **`JwtAuthFilter` runs before** Spring's own authentication filter

---

#### 7. `src/main/java/com/example/dto/LoginResponseDto.java` — NEW FILE

**Role:** A clean, typed response object returned by `/login`. Previously the controller returned a raw `Map`. This DTO adds the `token` field.

```java
public class LoginResponseDto {
    private boolean ok;
    private String message;
    private String token;      // ← the JWT
    private AccountInfo body;  // ← account details (id, name, balance, etc.)
}
```

---

#### 8. `src/main/java/com/example/controller/LoginController.java` — MODIFIED

**What changed:** After validating the password, the controller now calls `JwtUtil.generateToken()` and includes the token in the response.

```java
// BEFORE — returned a plain Map with account info
return Map.of("ok", true, "message", "Login successful", "body", Map.of(...));

// AFTER — generates JWT and returns it with account info
String token = jwtUtil.generateToken(account.getUsername(), account.getId());
return ResponseEntity.ok(new LoginResponseDto(true, "Login successful", token, accountInfo));
```

Password comparison remains **plaintext** (`.equals()`). Only the token issuance layer was added.

---

#### 9. `src/main/java/com/example/config/CorsConfig.java` — MODIFIED

Added `.exposedHeaders("Authorization")` so the browser can read the Authorization header in CORS responses from Angular.

---

### FRONTEND — Angular

---

#### 10. `src/app/components/services/auth.service.ts` — MODIFIED

**What changed:**
- `login()` — now extracts the `token` from the response and saves it to `localStorage`
- `getToken()` — reads the real JWT from `localStorage` (was returning `"<temp>"` before)
- `logout()` — clears the token from `localStorage`
- All `localStorage` calls wrapped with `isPlatformBrowser()` to support Angular SSR

```typescript
// After login succeeds:
localStorage.setItem('auth_token', res.token);

// getToken() now returns the real JWT:
return localStorage.getItem('auth_token');

// logout() clears it:
localStorage.removeItem('auth_token');
```

---

#### 11. `src/app/interceptors/jwt.interceptor.ts` — NEW FILE

**Role:** Runs automatically on **every outgoing HTTP request**. Reads the token from `localStorage` and injects it as an `Authorization` header. Also catches `401` responses and auto-logs-out.

```typescript
const token = auth.getToken();

if (token && !isPublicEndpoint) {
    const cloned = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);   // sends request WITH Authorization header
}
```

**This is why you see `Authorization: Bearer eyJ...` on every request in the Network tab — this file adds it.**

---

#### 12. `src/app/app.config.ts` — MODIFIED

Registered the JWT interceptor globally so it applies to all HTTP calls in the app:

```typescript
provideHttpClient(withFetch(), withInterceptors([jwtInterceptor]))
```

---

#### 13. `src/app/guards/auth-guard.ts` — MODIFIED

**What changed:** The guard now checks **both** the in-memory account signal AND the JWT token in localStorage. If the token is missing (e.g. old session before JWT was added), the user is redirected to login.

```typescript
const hasToken = !!auth.getToken();
const hasAccount = !!accountStore.getAccount();
const isAuthenticated = hasToken && hasAccount;  // BOTH required

if (!isAuthenticated) {
    if (!hasToken) accountStore.clear();
    return router.parseUrl('/');  // → redirect to login
}
```

---

#### 14. `src/app/components/services/account-store.service.ts` — MODIFIED

Added `isPlatformBrowser()` guard around `setInterval` polling so it doesn't run on the Node.js SSR server (which caused `localStorage is not defined` error).

---

## How the Token Flows — End to End

```
1. User fills login form
        │
        ▼
2. Angular → POST /login  { username, password }
        │
        ▼
3. LoginController validates password (.equals())
   JwtUtil.generateToken("john", 1)
   Returns: { ok: true, token: "eyJ...", body: {...} }
        │
        ▼
4. auth.service.ts receives response
   localStorage.setItem('auth_token', 'eyJ...')
        │
        ▼
5. User navigates to Dashboard
   authGuard checks: localStorage has token? ✅ → allow
        │
        ▼
6. AccountStore calls GET /accounts/1
   jwt.interceptor.ts reads token from localStorage
   Adds header: Authorization: Bearer eyJ...
        │
        ▼
7. Spring Boot receives GET /accounts/1
   JwtAuthFilter reads Authorization header
   JwtUtil.validateToken(token) → ✅ valid
   Extracts username "john" → loads from DB
   Sets SecurityContextHolder → request is authenticated
        │
        ▼
8. AccountController.getAccountById(1) runs → returns data
        │
        ▼
9. Same flow repeats for: POST /transfer, GET /transactionLogs, GET /rewards
   Every request carries the same token automatically
        │
        ▼
10. Token expires after 24 hours
    Next request → JwtUtil.validateToken() → false → not set in SecurityContext
    Spring Security → 401 Unauthorized
    jwt.interceptor.ts catches 401 → auth.logout() → navigate to /login
```

---

## Where to See JWT in the Browser

| What | Where to look |
|------|--------------|
| **Token issued on login** | Network tab → `POST login` → **Response tab** → `token` field |
| **Token sent on any API call** | Network tab → any request → **Headers tab** → **Request Headers** → `Authorization: Bearer ...` |
| **Token stored locally** | DevTools → **Application tab** → Local Storage → `auth_token` |
| **Token decoded** | Copy token → paste at **https://jwt.io** |

---

## Security Notes

- Passwords are stored as **plaintext** in the DB (existing design, Option A)
- JWT tokens are signed with HMAC-SHA256 — cannot be forged without the server secret
- Tokens expire after **24 hours** automatically
- Logging out removes the token from localStorage immediately
- All protected routes return `401 Unauthorized` without a valid token — no data leaks
