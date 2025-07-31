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
public class CallBackTransaction {
    @JsonProperty("id")
    private String transactionId;
    @JsonProperty("action")
    private String action;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("bet_amount")
    private double betAmount;
    @JsonProperty("channel_code")
    private String channelCode;
    @JsonProperty("round_id")
    private String roundId;
    @JsonProperty("wager_code")
    private String wagerCode;
    @JsonProperty("wager_status")
    private String wagerStatus;
    @JsonProperty("valid_bet_amount")
    private double validBetAmount;
    @JsonProperty("prize_amount")
    private double prizeAmount;
    @JsonProperty("tip_amount")
    private double tipAmount;
    @JsonProperty("Currency")
    private Currency currency;
    @JsonProperty("settle_at")
    private Long settleAt;
    @JsonProperty("payload")
    private Payload payload;
    @JsonProperty("game_code")
    private String gameCode;

}
