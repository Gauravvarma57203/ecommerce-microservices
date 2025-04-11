package com.ecommerce.auth.controller;

import com.ecommerce.auth.dto.*;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
@Valid
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(
            @RequestBody @Valid UserRequestRegistrationDto request,
            HttpServletRequest httpRequest) {

        ApiResponse<AuthResponse> response = userService.registerUser(request);
        addMeta(response, httpRequest, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(
            @RequestBody @Valid UserRequestLoginDto request,
            HttpServletRequest httpRequest) {

        ApiResponse<AuthResponse> response = userService.loginUser(request);
        addMeta(response, httpRequest, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserByEmail(
            @RequestParam String email,
            HttpServletRequest httpRequest) {

        ApiResponse<UserProfileDto> response = userService.getUserByEmail(email);
        addMeta(response, httpRequest, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest httpRequest) {

        ApiResponse<UserProfileDto> response = userService.getCurrentUser(userDetails.getUsername());
        addMeta(response, httpRequest, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is healthy 💚");
    }

    private <T> void addMeta(ApiResponse<T> response, HttpServletRequest request, HttpStatus status) {
        response.setPath(request.getRequestURI());
        response.setStatus(status.value());
        response.setTimestamp(Instant.now());
    }
}
