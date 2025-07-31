package com.security.spring.withdraw.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawStatusRequest {
    @NotEmpty(message = "Please Insert Withdraw Id")
    private Integer withdrawId;
    @NotEmpty(message = "Please Enter Withdraw status")
    private String withdrawStatus;
}
