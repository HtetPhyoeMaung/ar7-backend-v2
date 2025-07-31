package com.security.spring.gamesoft.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.spring.gamesoft.gameType.entity.GameType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameSoftWaggerRequest {

    @JsonProperty("WagerID")
    private long wagerID;

    @JsonProperty("MemberName")
    private String memberName;

    @JsonProperty("ProductID")
    private Long productID;

    @JsonProperty("GameType")
    @ManyToOne
    @JoinColumn(name="gametype_id")
    private GameType gameTypeId;

    @JsonProperty("CurrencyID")
    private int currencyID;

    @JsonProperty("GameID")
    private String gameID;

    @JsonProperty("GameRoundID")
    private String gameRoundID;

    @JsonProperty("ValidBetAmount")
    private double validBetAmount;

    @JsonProperty("BetAmount")
    private double betAmount;

    @JsonProperty("JPBet")
    private double jPBet;

    @JsonProperty("PayoutAmount")
    private double payoutAmount;

    @JsonProperty("CommisionAmount")
    private double commisionAmount;

    @JsonProperty("JackpotAmount")
    private double jackpotAmount;

    @JsonProperty("SettlementDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime settlementDate;

    @JsonProperty("Status")
    private int status;

    @JsonProperty("CreatedOn")
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDate createdOn;

    @JsonProperty("ModifiedOn")
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDate modifiedOn;
}
