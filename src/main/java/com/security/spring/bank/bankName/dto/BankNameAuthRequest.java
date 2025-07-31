package com.security.spring.bank.bankName.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankNameAuthRequest {
    private Integer bankTypeId;
    private Integer bankNameId;
    private Integer bankNameAuthId;
}
