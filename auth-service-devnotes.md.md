# 📘 AuthService Microservice - Summary (Till Day 4)

This document gives a comprehensive overview of everything we've done so far in our `auth-service` microservice. It is beginner-friendly and walks through every major component, error fix, command, and development flow step-by-step.

---

## ✅ 1. Classes Overview (with Responsibilities, Inheritance & Methods)

### 🧱 `User`

- **Type:** Entity class
- **Role:** Represents a user in the database.
- **Fields:** `id`, `name`, `email`, `password`
- **Annotations:** `@Entity`, `@Table`, etc.

### 🧰 `UserRepository`

- **Type:** Interface
- **Extends:** `JpaRepository<User, Long>`
- **Role:** Communicates with the database
- **Methods:**
    - `Optional<User> findByEmail(String email)` – Find a user using email.

### 📦 DTO Classes

#### `UserRequestDto`

- For receiving login or registration requests.
- Fields: `name`, `email`, `password`

#### `UserRegisterResponseDto`

- For response after successful registration.
- Fields: `id`, `name`, `email`, `message`

#### `UserLoginResponseDto`

- For response after successful login.
- Fields: `id`, `name`, `email`, `message`, `token`

#### `UserProfileDto`

- For `/me` endpoint to return user data without the token.
- Fields: `id`, `name`, `email`

### 💼 `UserService` Interface

- Declares service methods:
    - `UserRegisterResponseDto registerUser(UserRequestDto dto)`
    - `UserLoginResponseDto loginUser(UserRequestDto dto)`
    - `UserProfileDto getCurrentUser()`

### 🛠️ `UserServiceImpl`

- **Implements:** `UserService`
- **Contains business logic**:
    - Registration: Validate and save user.
    - Login: Validate password and generate JWT.
    - Get Current User: From `SecurityContextHolder`

### 👮‍♂️ `CustomUserDetails`

- **Implements:** `UserDetails`
- **Role:** Wraps our `User` entity to comply with Spring Security
- Methods:
    - `getAuthorities()`, `getUsername()`, `getPassword()` etc.

### 👨‍💻 `CustomUserDetailsService`

- **Implements:** `UserDetailsService`
- Loads user by email for authentication
- Method:
    - `loadUserByUsername(String email)`

### 🔐 `JwtUtil`

- Utility class for creating, validating, and parsing JWT tokens.
- Methods:
    - `generateToken(String email)`
    - `extractEmail(String token)`
    - `isTokenValid(String token, UserDetails userDetails)`

### 🧼 `JwtAuthFilter`

- **Extends:** `OncePerRequestFilter`
- Intercepts every request to:
    - Extract JWT
    - Validate token
    - Load user into `SecurityContext`

### 🔧 `SecurityConfig`

- Configures Spring Security
- Registers the `JwtAuthFilter`
- Allows `/register`, `/login`, and protects others

### 📡 `AuthController`

- Handles HTTP requests (REST controller)
- Endpoints:
    - `POST /register`
    - `POST /login`
    - `GET /me`

---

# ###🚨 Error Log & Fixes (Day 1–4) – `auth-service` Microservice

---

## 📅 **Day 1: Initial Setup**

| Issue # | Problem                                                                 | Solution                                                                 |
|--------|-------------------------------------------------------------------------|--------------------------------------------------------------------------|
| 1      | Spring Boot app used port `8080`, which was already occupied.           | Changed port to `8081` in `application-local.properties`:<br>`server.port=8081` |

---

## 📅 **Day 2: Password Encryption with BCrypt**

| Issue # | Problem                                            | Solution                                                                 |
|--------|-----------------------------------------------------|--------------------------------------------------------------------------|
| 2      | Passwords were saved in **plaintext** in PostgreSQL. | Used `BCryptPasswordEncoder` in `UserServiceImpl`:<br>`user.setPassword(passwordEncoder.encode(...))`<br>Also added a Bean in `SecurityConfig`:<br>`@Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }` |

---

## 📅 **Day 3: JWT & Docker Integration**

| Issue # | Problem                                                                 | Solution                                                                 |
|--------|--------------------------------------------------------------------------|--------------------------------------------------------------------------|
| 3      | Spring app couldn’t connect to Postgres in Docker (used `localhost`).    | Updated DB host to Docker service name: `db` →<br>`spring.datasource.url=jdbc:postgresql://db:5432/auth-db` |
| 4      | Conflict between **local and Docker** environments.                      | Added `.env` file + used `@Profile` (`application-local.properties`, `application-docker.properties`). |

---

## 📅 **Day 4: JWT Auth Flow**

| Issue # | Problem                                                                 | Solution                                                                 |
|--------|--------------------------------------------------------------------------|--------------------------------------------------------------------------|
| 5      | `/me` returned a response with `null` token.                             | Split DTOs:<br>- `UserLoginResponseDto` → with token<br>- `UserProfileResponseDto` → no token |
| 6      | Token wasn’t being returned on login.                                    | Logged token generation in `UserServiceImpl`, fixed response DTO.        |
| 7      | Password field allowed **weak values**.                                  | Added `@Pattern` regex in DTO:<br>Includes uppercase, lowercase, number, special character, min length |
| 8      | `/me` didn’t validate JWT correctly.                                     | Verified:<br>- `JwtAuthFilter` extracted email<br>- `CustomUserDetailsService` loaded user<br>- `SecurityContextHolder` stored auth |
| 9      | Token field was reused in multiple DTOs.                                 | Cleaned up DTOs:<br>- Login: `UserLoginResponseDto`<br>- Register: `UserRegisterResponseDto` |
| 10     | Validation errors returned **raw HTTP 400** responses.                   | Added `@ControllerAdvice` + Global Exception Handler to format error messages. |
| 11     | Docker Compose: Postgres not ready when Spring Boot started.            | Added `depends_on` in `docker-compose.yml` + `restart: always` policy.   |

---

## 🛠️ **Other Common Errors**

| Error                           | Solution                                                                 |
|--------------------------------|--------------------------------------------------------------------------|
| JWT signing key too short       | Used `Keys.secretKeyFor(SignatureAlgorithm.HS512)`                       |
| Token was `null` in `/me`       | Created separate DTOs for login and profile                              |
| JWT not set in SecurityContext  | Fixed `JwtAuthFilter` + registered it in `SecurityConfig`                |
| Local vs Docker DB mismatch     | Used `.env` + `application-{profile}.properties`                         |
| Weak password allowed           | Updated `@Pattern` regex with better constraints                         |

---

## 💻 Terminal Commands Used

### 🔸 Git Commands
```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin <your-repo-url>
git push -u origin main
```

### 🔸 Maven Commands
```bash
mvn clean install
mvn spring-boot:run
```

### 🔸 Docker & Docker Compose
```bash
docker compose up --build
docker compose down
```

### 🔸 PostgreSQL via Docker
```bash
docker exec -it <postgres-container-name> psql -U postgres -d auth-db
```

---



## 🧪 3. Commands Used (All Tools)

### ✅ Maven

```bash
mvn clean install
mvn spring-boot:run
```

### 🐳 Docker

```bash
docker-compose up -d
# Start services

docker ps
# List running containers

docker logs <container_id>
# Debug Postgres issues
```

### 🧪 Postman

- POST `/api/auth/register`
- POST `/api/auth/login`
- GET `/api/auth/me` (add `Authorization: Bearer <token>`)

### 💻 Git

```bash
git init
git add .
git commit -m "Initial Commit"
git checkout -b auth-service
git push origin auth-service
git status
```

---

## 🔁 4. Dev Flow (With Analogy)

### 🔐 Login Flow – **Dev View**

```
[Client Login]
   ⬇
[UserServiceImpl validates user, generates JWT]
   ⬇
[Return UserLoginResponseDto with token]
   ⬇
[Client stores token (like hotel keycard)]
```

### 👤 `/me` Flow – **Dev View**

```
[Client sends token in Authorization header]
   ⬇
[JwtAuthFilter reads and validates token]
   ⬇
[JwtUtil extracts email]
   ⬇
[CustomUserDetailsService loads user]
   ⬇
[SecurityContext holds current user]
   ⬇
[Controller fetches user from SecurityContext and returns UserProfileDto]
```

### 🏨 Analogy: JWT is like a hotel keycard 🛎️

- **Login = Check-In**: You show ID and get a keycard (JWT)
- **/me = Room Access**: You swipe your keycard (token), hotel system checks and lets you in
- **Registration = Booking**: You reserve your room but get your keycard only when you check-in

---

✅ Saved and documented everything till Day 4. We’re now ready for **Day 5: Role-Based Authorization + Admin/User Privileges**.

Let’s go! 🚀

