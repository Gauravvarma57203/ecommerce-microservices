package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.dto.*;
import com.ecommerce.auth.dto.legacy.UserRequestDto;
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
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public UserRegisterResponseDto registerUser(UserRequestRegistrationDto request) {
        // Check if email already exists
        userRepository.findByEmail(request.getEmail().toLowerCase())
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException("Email already exists");
                });

        // Create user
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(newUser);

        return UserRegisterResponseDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }


    @Override
    public UserProfileDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> UserProfileDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .message("User found")
                        .build())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }


    @Override
    public UserLoginResponseDto loginUser(UserRequestLoginDto request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new  InvalidCredentialsException("Invalid credentials");
        }
        // ✅ Generate JWT token

        String token;
        try {
            token = jwtUtil.generateToken(user.getEmail());
            System.out.println("Generated token: " + token);
        } catch (Exception e) {
            e.printStackTrace(); // print full error in logs
            throw new RuntimeException("Failed to generate token", e);
        }


        return UserLoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }

}
