package com.ecommerce.auth.service;


import com.ecommerce.auth.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    void deleteUserById(Long id);
}
