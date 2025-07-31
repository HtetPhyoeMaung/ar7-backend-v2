package com.security.spring.rro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUnit {
    private String adminId;
    private double mainUnit;
    private double gameUnit;
    private double promotionUnit;
}
