package com.security.spring.gamesoft.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameSoftTransitionRequest {
    @JsonProperty("MemberID")
    private Long memberId;
    @JsonProperty("OperatorID")
    private Long operatorID;
    @JsonProperty("ProductID")
    private Long productID;
    @JsonProperty("ProviderID")
    private Long providerID;
    @JsonProperty("ProviderLineID")
    private Long providerLineID;
    @JsonProperty("WagerID")
    private Long wagerID;
    @JsonProperty("CurrencyID")
    private Integer currencyID;
    @JsonProperty("GameType")
    private int gameType;
    @JsonProperty("GameID")
    private String gameID;
    @JsonProperty("GameRoundID")
    private String gameRoundID;
    @JsonProperty("ValidBetAmount")
    private double validBetAmount;
    @JsonProperty("BetAmount")
    private double betAmount;
    @JsonProperty("TransactionAmount")
    private double transactionAmount;
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("PayoutAmount")
    private double payoutAmount;
    @JsonProperty("PayoutDetail")
    private String payoutDetail;
    @JsonProperty("BetDetail")
    private String betDetail;
    @JsonProperty("CommissionAmount")
    private double commisionAmount;
    @JsonProperty("JackpotAmount")
    private double jackpotAmount;
    @JsonProperty("SettlementDate")
    private LocalDateTime settlementDate;
    @JsonProperty("JPBet")
    private double jPBet;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("CreatedOn")
    private LocalDateTime createdOn;
    @JsonProperty("ModifiedOn")
    private LocalDateTime modifiedOn;
    @JsonProperty(value = "Data")
    private String data;
    @JsonProperty("Fee")
    private double fee;
}
