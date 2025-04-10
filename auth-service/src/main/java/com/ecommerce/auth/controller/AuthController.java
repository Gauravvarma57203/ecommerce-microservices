package com.ecommerce.auth.controller;

import com.ecommerce.auth.dto.*;
import com.ecommerce.auth.dto.legacy.UserRequestDto;
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

    public AuthController(UserService userService ,UserRepository userRepository) {
        this.userService = userService;
        this.userRepository=userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid UserRequestRegistrationDto request,
                                                    HttpServletRequest httpRequest) {

        UserRegisterResponseDto responseData = userService.registerUser(request);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("User registered successfully")
                .status(HttpStatus.CREATED.value())
                .path(httpRequest.getRequestURI())
                .data(responseData)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/user")
    public ResponseEntity<UserProfileDto> getUserByEmail(@RequestParam String email) {
        UserProfileDto user = userService.getUserByEmail(email);
        if (user != null)
            return ResponseEntity.ok(user);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(
            @RequestBody @Valid UserRequestLoginDto requestDto,
            HttpServletRequest httpRequest) {

        UserLoginResponseDto loginData = userService.loginUser(requestDto);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .status(HttpStatus.OK.value())
                .path(httpRequest.getRequestURI())
                .data(loginData)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is healthy 💚");
    }

    @GetMapping("/me")
    public UserProfileDto getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .message("Current user details fetched successfully")
                .build();


    }
}
