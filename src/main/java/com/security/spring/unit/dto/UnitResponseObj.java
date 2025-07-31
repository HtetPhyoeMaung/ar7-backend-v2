package com.security.spring.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnitResponseObj {
    private int unitId;
    private double mainUnit;
    private double gameUnit;
    private double promotionUnit;
    private int tickets;
}
