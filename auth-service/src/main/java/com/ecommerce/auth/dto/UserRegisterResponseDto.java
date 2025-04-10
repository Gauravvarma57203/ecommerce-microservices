package com.ecommerce.auth.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponseDto {
    private Long id;
    private String name;
    private String email;
    private String message;
}
