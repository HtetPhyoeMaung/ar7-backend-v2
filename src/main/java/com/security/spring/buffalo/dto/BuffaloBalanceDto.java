package com.security.spring.buffalo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BuffaloBalanceDto {

    private String agentCode;

    private String playerId;

    private double balance;

    public static BuffaloBalanceDto of(String ar7Id, String agentCode, double balance) {
        return BuffaloBalanceDto.builder()
                .playerId(ar7Id)
                .agentCode(agentCode)
                .balance(balance)
                .build();
    }
}
