package com.security.spring.thirdpartygames.gameprovider.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortGameProviderRequest {
    @NotBlank(message = "gameTypeCode is required")
    private String gameTypeCode;

    @Valid
    @NotEmpty(message = "At least one provider sort item is required")
    private List<ProviderSortItem> providers;
}
