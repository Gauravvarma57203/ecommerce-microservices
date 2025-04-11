package com.ecommerce.auth.dto.legacy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseLoginDto {
    private Long id;
    private String name;
    private String email;
    private String token;
}
