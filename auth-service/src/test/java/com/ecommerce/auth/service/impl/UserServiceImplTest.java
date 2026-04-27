package com.ecommerce.auth.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.auth.dto.ApiResponse; // ✅ If your service now returns ApiResponse
import com.ecommerce.auth.dto.AuthResponse;
import com.ecommerce.auth.dto.UserRequestLoginDto; // ✅ New login request DTO
import com.ecommerce.auth.exception.InvalidCredentialsException;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtutil;

    // ✅ Test 1: Happy path
    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        // Arrange
        UserRequestLoginDto request = new UserRequestLoginDto(); // ✅ Updated DTO
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);
        // when(jwtutil.generateToken(user)).thenReturn("mock.jwt.token");

        // Act
        ApiResponse<AuthResponse> response = userService.loginUser(request); // ✅ Updated return type

        // Assert
        assertNotNull(response);
        assertEquals("Login successful", response.getMessage()); // ✅ Moved to wrapper response
        assertEquals("test@example.com", response.getData().getEmail()); // ✅ Get data from ApiResponse<UserLoginResponseDto>
        // assertEquals("mock.jwt.token", response.getData().getToken()); // if token is returned
    }

    // ✅ Test 2: User not found
    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        UserRequestLoginDto request = new UserRequestLoginDto(); // ✅ Updated DTO
        request.setEmail("nouser@example.com");
        request.setPassword("irrelevant");

        when(userRepository.findByEmail("nouser@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(request));
        assertEquals("User not found", ex.getMessage());
    }

    // ✅ Test 3: Wrong password
    @Test
    void login_ShouldThrowException_WhenPasswordIsWrong() {
        // Arrange
        UserRequestLoginDto request = new UserRequestLoginDto(); // ✅ Updated DTO
        request.setEmail("test@example.com");
        request.setPassword("wrongpass");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "encodedPass")).thenReturn(false);

        // Act & Assert
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(request));
        assertEquals("Invalid credentials", ex.getMessage());
    }
}
