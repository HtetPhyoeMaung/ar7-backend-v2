package com.security.spring.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.spring.promotion.dto.TicketBoxResponse;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseObj {
    private String userName;
    private String userEmail;
    private String userPhone;
    private String ar7Id;
    private Role role;
    private LocalDateTime lastLoginTime;
    private String profileImage;
    private boolean userStatus;
    private UserUnits userUnits;
//    private CommissionRe userComession;
    private String secretCode;
    private String parentUserId;
    private TicketBoxResponse ticketBoxResponse;
}
