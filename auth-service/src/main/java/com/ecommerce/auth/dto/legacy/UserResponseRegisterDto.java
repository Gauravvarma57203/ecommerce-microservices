package com.ecommerce.auth.dto.legacy;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseRegisterDto {
    private Long id;
    private String name;
    private String email;
    private String message;
}
