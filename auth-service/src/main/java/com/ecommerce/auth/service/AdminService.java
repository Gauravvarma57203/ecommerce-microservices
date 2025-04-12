package com.ecommerce.auth.service;


import com.ecommerce.auth.dto.PaginatedResponse;
import com.ecommerce.auth.dto.UserDto;

import java.util.List;

public interface AdminService {
    PaginatedResponse<UserDto> getAllUsers(int page, int size);

    UserDto getUserById(Long id);
    void deleteUserById(Long id);
}
