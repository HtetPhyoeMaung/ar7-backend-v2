package com.security.spring.gamebank.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class GameBankBuyScatterRequest {
    @NotBlank(message = "gameType must not be null or blank!")
    private String gameType;
    @NotBlank(message = "gameName must not be null or blank!")
    private String gameName;
    @NotBlank(message = "agentId must not be null or blank!")
    private String agentId;
    @NotBlank(message = "agentCode must not be null or blank!")
    private String agentCode;
    @NotBlank(message = "playerId must not be null or blank!")
    private String playerId;
    @PositiveOrZero(message = "userDeductBalance must be zero or a positive number!")
    @JsonProperty(value = "userDeductBalance", required = false)
    private double userDeductBalance;
    // accept legacy payloads that send "userBalance" meaning deduction amount
    @JsonProperty(value = "userBalance", required = false)
    private Double legacyUserBalance;
  
    private String transactionId;
  
    private String createdAt;
}

