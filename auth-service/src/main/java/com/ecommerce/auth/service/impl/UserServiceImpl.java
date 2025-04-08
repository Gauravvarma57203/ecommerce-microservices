package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.dto.UserRequestDto;
import com.ecommerce.auth.dto.UserResponseDto;
import com.ecommerce.auth.exception.EmailAlreadyExistsException;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.repository.UserRepository;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponseDto registerUser(UserRequestDto request) {
        // Check if the email already exists
        if (userRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
            throw new EmailAlreadyExistsException("This email is already registered. Try logging in or use another email.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase()) // Normalize email to lowercase
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }


    @Override
    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .orElse(null);
    }

    @Override
    public UserResponseDto loginUser(UserRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
