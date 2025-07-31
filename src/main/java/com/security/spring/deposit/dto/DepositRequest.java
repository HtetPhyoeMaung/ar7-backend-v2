package com.security.spring.deposit.dto;

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
public class DepositRequest {

    private Long depositId;
    private String depositStatus;

    @NotEmpty(message = "Please Enter Your transfer account")
    private String accountNumber;
    private String accountName;

    @NotNull(message = "Amount Fields Required")
    @Positive
    private double amount;

    @NotNull(message = "Bank Account Fields Required")
    @Positive
    private Integer parentBankAccId;

    @NotEmpty
    @NotNull(message = "Transition Id last 6 digit is Required")
    private String userTransitionId;
}
