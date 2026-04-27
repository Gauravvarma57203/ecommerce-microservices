# Dockerfile inside auth-service/

# Step 1: Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk-jammy

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy the jar file from target/
COPY target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar

# Step 4: Expose the port
EXPOSE 8080

# Step 5: Command to run the app
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
