package com.security.spring.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUnitRequest {
    @NotNull(message = "Please Enter Create Unit Amount")
    @Min(value = 0, message = "Unit amount must be greater than or equal to 0")
    @Max(value = 999999999, message = "Unit amount must be less than or equal to 999999999")
    private double unitAmt;
}
