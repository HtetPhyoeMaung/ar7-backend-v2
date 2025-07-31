package com.security.spring.withdraw.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequest {
    @NotNull(message = "Please Enter BankName Id")
    private Integer bankNameId;
    @NotNull
    @NotEmpty(message = "Please Enter Your Account for withdraw")
    private String withdrawAccount;
    @NotNull
    @Positive(message = "Please Enter Unit Amount want to withdraw")
    private double withdrawAmount;
    private String withdrawAccName;
    private String withdrawTransitionNumber;
}
