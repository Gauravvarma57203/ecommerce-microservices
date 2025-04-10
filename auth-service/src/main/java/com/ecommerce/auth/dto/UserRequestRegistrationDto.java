package com.ecommerce.auth.dto;

import com.ecommerce.auth.validation.ValidEmailDomain;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestRegistrationDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @ValidEmailDomain(
            allowed = {
                    "gmail.com", "yahoo.com", "outlook.com",
                    "protonmail.com", "zoho.com", "hotmail.com", "icloud.com"
            }
    )
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{5,}$",
            message = "Password must contain at least 1 lowercase, 1 uppercase, 1 digit, 1 special character and be at least 5 characters long"
    )
    private String password;
}
