package com.security.spring.bank.bankType.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankTypeRequest {
    private Long bankTypeId;
    @NotEmpty(message = "Please Enter Bank Type Name")
    private String bankTypeName;
}
