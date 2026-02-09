package com.security.spring.hotgames.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddHotGameRequest {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Game code is required")
    private String gameCode;

    @NotBlank(message = "Game type is required")
    private String gameType;

    private Integer productId;

    @NotNull(message = "Product code is required")
    private Integer productCode;

    @NotNull(message = "Sort order is required")
    @Min(value = 0, message = "Sort order must be at least 0")
    private Integer sortOrder;
}