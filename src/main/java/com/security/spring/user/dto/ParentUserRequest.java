package com.security.spring.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParentUserRequest {
    @NotEmpty(message = "Please Insert Parent Ar7 Id")
    private String parentAr7Id;
}
