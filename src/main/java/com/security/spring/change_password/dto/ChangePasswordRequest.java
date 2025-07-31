package com.security.spring.change_password.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {

    private String userAr7Id;
    private String currentPassword;

    @NotNull(message = "new password must not be null")
    @NotEmpty(message = "new password must not be empty")
    @Size(min = 6, message = "new password must have at least 6 characters")
    private String newPassword;

    @NotNull(message = "By change status must not be null")
    @NotEmpty(message = "By change status must not be empty")
    private ByChange byChange;

    public enum ByChange{
        BY_THEMSELF,
        BY_ADMIN
    }
}
