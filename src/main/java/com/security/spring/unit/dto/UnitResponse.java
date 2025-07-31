package com.security.spring.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnitResponse {
    private String message;
    private Integer statusCode;
    private Boolean status;
    private UnitResponseObj unitResponseObj;
}
