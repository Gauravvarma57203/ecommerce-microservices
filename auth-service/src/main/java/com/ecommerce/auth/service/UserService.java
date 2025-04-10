package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.*;
import com.ecommerce.auth.dto.legacy.UserRequestDto;

public interface UserService {

    UserProfileDto getUserByEmail(String email);
    UserLoginResponseDto loginUser(UserRequestLoginDto request);

    UserRegisterResponseDto registerUser(UserRequestRegistrationDto request);


}
