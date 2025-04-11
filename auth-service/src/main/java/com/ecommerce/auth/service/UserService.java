package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.*;
import com.ecommerce.auth.dto.legacy.UserResponseRegisterDto;

public interface UserService {

    ApiResponse<UserProfileDto> getCurrentUser(String email);
    ApiResponse<UserProfileDto> getUserByEmail(String email);


    ApiResponse<AuthResponse> loginUser(UserRequestLoginDto request);
    ApiResponse<AuthResponse> registerUser(UserRequestRegistrationDto request);


}
