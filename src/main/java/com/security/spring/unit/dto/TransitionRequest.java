package com.security.spring.unit.dto;

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
public class TransitionRequest {
    @NotEmpty(message = "Please enter your secret code")
    private String secretCode;
    @Positive(message = "please enter amount more then 0")
    private double amount;
    @NotEmpty(message = "Please collect want to transfer user")
    private String toAr7UserId;
    private String remark;
}
