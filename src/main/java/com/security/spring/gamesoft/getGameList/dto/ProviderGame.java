package com.security.spring.gamesoft.getGameList.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderGame {

    @JsonProperty("game_code")
    private String gameCode;

    @JsonProperty("game_name")
    private String gameName;

    @JsonProperty("game_type")
    private String gameType; // Fix: was int, should be String

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("product_code")
    private int productCode; // Fix: add this field

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("support_currency")
    private String supportCurrency;

    @JsonProperty("status")
    private String status;
}
