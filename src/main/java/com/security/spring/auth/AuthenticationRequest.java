package com.security.spring.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationRequest {
    @NotBlank(message = "ar7Id is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "ar7Id must not contain special characters"
    )
    private String ar7Id;
    private String password;
}
