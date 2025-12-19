package com.security.spring.gamebank.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameBankBalanceResponse {

    private String agentCode;

    private String playerId;

    private double balance;

    public static GameBankBalanceResponse of(String ar7Id, String agentCode, double balance) {
        return GameBankBalanceResponse.builder()
                .playerId(ar7Id)
                .agentCode(agentCode)
                .balance(balance)
                .build();
    }
}
