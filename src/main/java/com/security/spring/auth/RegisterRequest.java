package com.security.spring.auth;

import com.security.spring.user.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String fullName;
    private String password;
    private Role role;
    private String secretCode;
    private String parentUserId;
}
