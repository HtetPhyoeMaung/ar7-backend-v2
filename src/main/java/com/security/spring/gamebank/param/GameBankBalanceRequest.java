package com.security.spring.gamebank.param;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GameBankBalanceRequest {
    @NotBlank(message = "agent_code must not be null or blank!")
    private String agentCode;
    @NotBlank(message = "password must not be null or blank!")
    private String agentId;
    @NotBlank(message = "player_id must not be null or blank!")
    private String playerId;
}
