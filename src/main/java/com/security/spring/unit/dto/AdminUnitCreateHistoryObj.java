package com.security.spring.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUnitCreateHistoryObj {
    private double amount;
    private LocalDateTime createTime;
}
