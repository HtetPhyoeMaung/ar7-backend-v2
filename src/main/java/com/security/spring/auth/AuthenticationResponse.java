package com.security.spring.auth;

import com.security.spring.user.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationResponse {
    private String refreshToken;
    private String name;
    private String token;
    private Role role;
    private String secretCode;
    private String ar7Id;
    private String parentAr7Id;
    private String password;
    private String message;
    private Boolean status;
    private Map<String, String> component;
}
