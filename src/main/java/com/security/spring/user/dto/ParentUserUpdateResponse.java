package com.security.spring.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParentUserUpdateResponse {
    private String message;
    private Integer statusCode;
    private Boolean status;
}
