package com.security.spring.change_password.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeAr7IdResponse {
    private Boolean status;
    private String message;
    private String oldAr7Id;
    private String newAr7Id;
}
