CHANGELOG

All notable changes to this project will be documented here.
## 📅 Day 5 – Changelog (April 10, 2025)

### ✅ What Was Done

- 📦 **Created Separate DTOs**
  - `UserRegisterRequestDto.java` – Handles registration input
  - `UserRequestLoginDto.java` – Handles login input

- 📄 **Created Generic API Response Wrapper**
  - `ApiResponse<T>` – Wraps responses with a message, data, and success flag

- 🧹 **Refactored Request & Response Handling**
  - Cleaned and separated logic for registration and login
  - Improved code readability and structure


## 📅 Day 4 – April 9, 2025
### ✅ Module: `auth-service`

---

### 🔧 Work Completed

- ✅ **Full Integration Testing** completed on `auth-service`
  - Real PostgreSQL DB used (via Docker)
  - Auth flow tested end-to-end using `UserServiceImpl`, `JwtUtil`, and controller layer

- ✅ **Login Flow**
  - Valid email + password → `AuthResponse` with JWT
  - Case-sensitive email check:
    - `UPPERCASE@EMAIL.com` → 404 (User Not Found)
    - `lowercase@email.com` → 200 OK (User Found)

- ✅ **Validation & Security**
  - Password length constraints validated via `@Size` in `LoginRequest`
  - Custom error messages returned via `GlobalExceptionHandler`
  - Invalid credentials return appropriate `401` error response

- ✅ **/me Endpoint Testing**
  - JWT sent in `Authorization: Bearer <token>` header
  - `/me` returns full user object using `SecurityContextHolder`
  - Missing/invalid/expired JWT → 401 Unauthorized

---

### 🐳 Dockerized Setup

- ✅ `docker-compose.yml` used to spin up:
  - `auth-service` container (`ecommerce-microservices-auth-service`)
  - `Postgres:15` container (persisted with volume)
- ✅ `.env` file used for:
  - `SPRING_DATASOURCE_*`
  - `JWT_SECRET`, `JWT_EXPIRATION`, etc.
- ✅ `.env` file added to `.gitignore`
- ✅ `.env.example` created with placeholders for safe sharing/versioning
- ✅ Docker container behavior matches local Spring Boot app exactly (ports, DB, JWT, responses)

---

### 🚀 Auth Flow (Technical Dev Flow)

```text
[Client sends LoginRequest (email, password)] 
⬇  
[UserServiceImpl.loginUser()]  
  ↳ Validates credentials  
  ↳ Loads user via [UserRepository.findByEmail()]  
  ↳ Calls [JwtUtil.generateToken()] → creates JWT  
⬇  
[AuthResponse(email, token)] sent to client  
⬇  
Client stores JWT in memory/localStorage  
⬇  
Client hits [/api/auth/me] with JWT in Authorization header  
⬇  
[JwtAuthFilter] intercepts request  
  ↳ Extracts token → passes to [JwtUtil.validateToken()]  
  ↳ Extracts email from token → loads user via  
    [CustomUserDetailsService.loadUserByUsername()]  
⬇  
[SecurityContextHolder] stores authenticated user  
⬇  
[UserController.getCurrentUser()] reads from context → returns user info

## [Day 3] - 2025-04-08

### Added
- ✅ `UserResponseDto` updated to include `message` field for consistent client responses.
- ✅ Global Exception Handling using `@RestControllerAdvice` for:
    - Validation errors (`@Valid`)
    - `EmailAlreadyExistsException`
    - `InvalidCredentialsException`
    - Generic fallback errors
- ✅ `.env` support added for environment-specific config in Docker
- ✅ Error message added for `GET /api/auth/user?email=` when user not found
- ✅ `InvalidCredentialsException` replaced generic runtime exception in login
- ✅ Test coverage for invalid credentials and not-found scenarios

### Changed
- 🔁 Refactored `AuthServiceImpl`:
    - `loginUser()` now throws `InvalidCredentialsException`
    - `getUserByEmail()` now returns custom error if user not found
- 🧪 Improved test coverage using `@SpringBootTest` + assertions on exception types

### Fixed
- 🐛 Fixed issue where `message` field was returning `null` in success response
- 🐛 Improved response structure for missing users

### Pending / Deferred
- ❌ GitHub Actions (CI) - Scheduled for later (post Day 4)







[Day 2] - 2025-04-07

✨ Added

UserService with secure registration and login methods.

DTO layer (UserRequestDto, UserResponseDto) for clean data exchange.

Password hashing with BCryptPasswordEncoder.

Validation logic using @Valid annotations on DTOs.

Global exception handler to return user-friendly error messages.

Custom error messages for duplicate email registration.

Spring Boot Profiles: local and docker using application-local.properties and application-docker.properties.

Environment variables managed via .env file for Docker.

Connected Spring Boot to Dockerized PostgreSQL using Docker Compose.

Tested API endpoints via Postman (local and Dockerized setup).

🐳 DevOps & Docker

Created Dockerfile for auth-service.

Updated docker-compose.yml to support multi-service setup with PostgreSQL.

Added profile-based property loading for local vs containerized runs.

Ensured seamless switching between local and Docker environments.






[Day 1] - 2025-04-06

🛠️ Initial Setup

Initialized Git repository and committed base project structure.

Created auth-service as a standalone microservice using Spring Boot.

⚙️ Development Setup

Configured Maven build tool and pom.xml with required dependencies:

Spring Web

Spring Security

Spring Data JPA

Lombok

Hibernate Validator

PostgreSQL Driver

Implemented basic domain model:

User entity

UserRepository for database interaction

Set up project structure with clear separation of concerns (entities, repositories, services, controllers).

Configured application.properties with local database connection and port setup.

🐳 DevOps & Docker

Installed and configured Docker and Docker Compose.

Pulled and ran PostgreSQL container for local development.

Tested database connectivity using Postman and Spring Boot app.

Discussed and decided on using profiles (local, docker) for better config management in upcoming days.