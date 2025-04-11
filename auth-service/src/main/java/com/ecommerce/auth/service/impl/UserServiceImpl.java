package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.dto.*;
import com.ecommerce.auth.exception.EmailAlreadyExistsException;
import com.ecommerce.auth.exception.InvalidCredentialsException;
import com.ecommerce.auth.exception.UserNotFoundException;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.security.JwtUtil;
import com.ecommerce.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Valid
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ApiResponse<AuthResponse> registerUser(UserRequestRegistrationDto request) {
        userRepository.findByEmail(request.getEmail().toLowerCase())
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException("Email already exists");
                });

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(newUser);

        String token = generateJwt(savedUser.getEmail());

        AuthResponse authResponse = AuthResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .token(token)
                .build();

        return ApiResponse.success("User registered successfully", authResponse);
    }

    @Override
    public ApiResponse<AuthResponse> loginUser(UserRequestLoginDto request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = generateJwt(user.getEmail());

        AuthResponse authResponse = AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();

        return ApiResponse.success("Login successful", authResponse);
    }

    @Override
    public ApiResponse<UserProfileDto> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        UserProfileDto profileDto = UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .message("User found")
                .build();

        return ApiResponse.success("User fetched successfully", profileDto);
    }

    // ✅ This can be used in /me
    public ApiResponse<UserProfileDto> getCurrentUser(String email) {
        return getUserByEmail(email); // You already have a solid reusable method!
    }

    private String generateJwt(String email) {
        try {
            String token = jwtUtil.generateToken(email);
            System.out.println("Generated token: " + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate token", e);
        }
    }
}
