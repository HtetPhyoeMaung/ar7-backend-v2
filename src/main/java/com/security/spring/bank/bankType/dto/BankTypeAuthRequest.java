package com.security.spring.bank.bankType.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTypeAuthRequest {
    private Long bankTypeId;
}
