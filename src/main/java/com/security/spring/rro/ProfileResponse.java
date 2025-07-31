package com.security.spring.rro;

import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
        private String name;
        private String email;
        private String ar7Id;
        private Role role;
        private LocalDateTime lastLoginTime;
        private String profileImage;
        private boolean status;
        private UserUnits userUnits;
//        private Comession userComession;
        private String secretCode;
        private String parentUserId;
}
