package com.security.spring.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUnitObj {
    private String message;
    private Integer statusCode;
    private Boolean status;
    private double mainUnit;
    private double gameUnit;
    private double promotionUnit;
    private double tickets;
}
