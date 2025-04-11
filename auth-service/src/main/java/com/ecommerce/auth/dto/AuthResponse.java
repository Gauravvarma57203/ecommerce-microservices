package com.ecommerce.auth.dto;

import com.ecommerce.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Long id;       // Required for .id(...) in builder
    private String name;
    private String email;
    private String token;
    private Role role;
}
