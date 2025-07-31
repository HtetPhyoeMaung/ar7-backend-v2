package com.security.spring.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomErrorResponse {
    private String message;
    private boolean status;
    private int statusCode;
}
