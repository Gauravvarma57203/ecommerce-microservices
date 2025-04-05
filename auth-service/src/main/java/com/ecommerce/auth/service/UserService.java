package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.UserRequestDto;
import com.ecommerce.auth.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto request);
    UserResponseDto getUserByEmail(String email);
}
