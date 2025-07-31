package com.security.spring.bank.bankAcc.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccStatusRequest {
    @NotEmpty(message = "Please Insert Bank Acc Id")
    private Integer bankAccId;
}
