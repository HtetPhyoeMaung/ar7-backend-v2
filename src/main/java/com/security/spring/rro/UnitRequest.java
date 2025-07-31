package com.security.spring.rro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitRequest {
    @NotEmpty
    @NotNull
    private String toAccount;

    @NotNull
    @Min(value = 0)
    private Double amount;

    @NotNull
    @NotEmpty
    private String secretCode;

    private String remark;
}
