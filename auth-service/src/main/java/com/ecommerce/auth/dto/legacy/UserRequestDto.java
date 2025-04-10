package com.ecommerce.auth.dto.legacy;

import com.ecommerce.auth.validation.ValidEmailDomain;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

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
    @Size(min = 5, message = "Password must be at least 5 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$",
            message = "Password must contain at least one uppercase, one lowercase, one number, one special character, and be at least 5 characters long"
    )
    private String password;

}
