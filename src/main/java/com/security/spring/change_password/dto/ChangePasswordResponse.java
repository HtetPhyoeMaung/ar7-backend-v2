package com.security.spring.change_password.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordResponse {
    private String ar7Id;
    private String changedPassword;
    private String message;
    private ChangePasswordRequest.ByChange byChange;
    private String byAdminId;
    private Boolean status;
}
