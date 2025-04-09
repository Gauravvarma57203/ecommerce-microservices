package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.UserProfileDto;
import com.ecommerce.auth.dto.UserRegisterResponseDto;
import com.ecommerce.auth.dto.UserRequestDto;
import com.ecommerce.auth.dto.UserLoginResponseDto;

public interface UserService {
    UserRegisterResponseDto registerUser(UserRequestDto request);
    UserProfileDto getUserByEmail(String email);
    UserLoginResponseDto loginUser(UserRequestDto requestDto);

}
