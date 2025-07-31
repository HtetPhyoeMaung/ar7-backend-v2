package com.security.spring.gamesoft.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WagerRequest {

    @JsonProperty("member_account")
    private String memberAccount;

    @JsonProperty("bet_amount")
    private String betAmount;

    @JsonProperty("valid_bet_amount")
    private String validBetAmount;

    @JsonProperty("prize_amount")
    private String prizeAmount;

    @JsonProperty("tip_amount")
    private String tipAmount;

    @JsonProperty("wager_type")
    private String wagerType;

    @JsonProperty("wager_code")
    private String wagerCode;

    @JsonProperty("wager_status")
    private String wagerStatus;

    @JsonProperty("round_id")
    private String roundId;

    @JsonProperty("channel_code")
    private String channelCode;

    @JsonProperty("game_type")
    private String gameType;

    @JsonProperty("settled_at")
    private long settledAt;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("payload")
    private Payload payload;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("game_code")
    private String gameCode;

    @JsonProperty("currency")
    private String currency;
}
