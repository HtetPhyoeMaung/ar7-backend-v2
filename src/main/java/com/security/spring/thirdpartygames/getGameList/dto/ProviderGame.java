package com.security.spring.thirdpartygames.getGameList.dto;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.spring.thirdpartygames.callback.dto.GameBankResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ProviderGame {

	@JsonProperty("game_code")
	@JsonAlias({ "code" })
	private String gameCode;

	@JsonProperty("game_name")
	@JsonAlias({ "name" })
	private String gameName;

	@JsonProperty("game_type")
	@JsonAlias({ "type" })
	private String gameType; // Fix: was int, should be String

	@JsonProperty("product_id")
	private int productId;

	@JsonProperty("product_code")
	private int productCode; // Fix: add this field

	@JsonProperty("image_url")
	@JsonAlias({ "imageUrl" })
	private String imageUrl;

	@JsonProperty("support_currency")
	@JsonAlias({ "currency" })
	private String supportCurrency;

	@JsonProperty("status")
	private String status;
	private String platform;
	private String gameUrl;
	private String description;
}
