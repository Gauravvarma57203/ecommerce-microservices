package com.ecommerce.auth.controller;

import com.ecommerce.auth.dto.UserProfileDto;
import com.ecommerce.auth.dto.UserRegisterResponseDto;
import com.ecommerce.auth.dto.UserRequestDto;
import com.ecommerce.auth.dto.UserLoginResponseDto;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserRegisterResponseDto> registerUser(@RequestBody @Valid  UserRequestDto requestDto) {
        UserRegisterResponseDto responseDto = userService.registerUser(requestDto);
        return ResponseEntity.ok(responseDto);
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
    public ResponseEntity<UserLoginResponseDto> loginUser(@RequestBody UserRequestDto requestDto) {
        UserLoginResponseDto response = userService.loginUser(requestDto);
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
