package com.security.spring.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketBoxResponse {
    private Long id;
    private long ticketAmount;
    private double outlierValue;
}
