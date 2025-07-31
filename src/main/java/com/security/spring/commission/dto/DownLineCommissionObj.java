package com.security.spring.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownLineCommissionObj {
    private int id;
    private String ar7Id;
    private String gameTypeName;
    private int gameTypeId;
    private double commissionPercentage;
}
