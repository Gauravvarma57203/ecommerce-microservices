CHANGELOG

All notable changes to this project will be documented here.









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