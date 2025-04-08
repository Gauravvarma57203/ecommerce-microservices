# ecommerce-microservices
Auth-Service Microservice

This is the auth-service microservice for a larger microservices-based e-commerce platform.
It handles user registration, login, password encryption, and serves as the authentication gateway.

🚀 Features

User registration and login

Input validation using @Valid annotations

Password encryption with BCrypt

Global exception handling with custom error messages

Environment-specific profiles (local, docker)

Integrated with PostgreSQL

Containerized using Docker and orchestrated with Docker Compose

📁 Project Structure

📦auth-service
├── src
│   ├── main/java/... (standard Spring Boot structure)
│   └── resources
│       ├── application.properties
│       ├── application-local.properties
│       └── application-docker.properties
├── Dockerfile
├── .env
├── docker-compose.yml
└── README.md

🔧 Configuration

application.properties

spring.application.name=auth-service
spring.profiles.active=local

application-local.properties

server.port=8081
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/auth_db
spring.datasource.username=postgres
spring.datasource.password=postgres

application-docker.properties

server.port=8080
spring.datasource.url=jdbc:postgresql://db:5432/auth_db
spring.datasource.username=postgres
spring.datasource.password=postgres

.env

SPRING_PROFILES_ACTIVE=docker

🐳 Docker & DevOps Setup

Dockerfile (for auth-service)

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "auth-service.jar"]

docker-compose.yml

version: '3.8'

services:
db:
image: postgres:15
container_name: postgres
environment:
POSTGRES_DB: auth_db
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres
ports:
- "5432:5432"
volumes:
- pgdata:/var/lib/postgresql/data
networks:
- backend

auth-service:
build:
context: ./auth-service
container_name: auth-service
ports:
- "8080:8080"
env_file:
- .env
depends_on:
- db
networks:
- backend

volumes:
pgdata:

networks:
backend:

🛠️ Build and Run

Build the JAR

./mvnw clean package

✅ No need to specify -Dspring.profiles.active=docker, the active profile is controlled at runtime.

Run with Docker

docker-compose up --build -d

This will:

Start PostgreSQL container

Build and run auth-service container using Dockerfile

Automatically apply docker profile using .env

Run Locally (without Docker)

./mvnw spring-boot:run -Dspring-boot.run.profiles=local

Or set it in your IDE as a Spring Boot profile (local).

📫 API Testing

Use Postman or any HTTP client to test endpoints:

Local: http://localhost:8081/api/auth/...

Docker: http://localhost:8080/api/auth/...

📦 Deployment Ready

Easily deployable in containers.

Profiles and properties are decoupled — works across environments.

Docker Compose ensures consistent multi-service orchestration.

📄 Changelog

See CHANGELOG.md for day-wise updates and history.

✅ TODO (Upcoming)

Add JWT-based authentication

Setup CI/CD pipeline using GitHub Actions

Add health checks and monitoring

Add Swagger API documentation

🙌 Maintained by

You — the one-dev-army 💪

“One day at a time. One commit at a time. One service at a time.”