package com.security.spring.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpaceTechGameTransactionObj {
    private String id;
    private int bet;
    private int amount;
    private String status;
    private int commission;

}
