package com.security.spring.gamebank.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class GameBankResultRequest {
    @NotBlank(message = "agent_code must not be null or blank!")
    private String agentCode;
    @NotBlank(message = "agent_id must not be null or blank!")
    private String agentId;
    @NotBlank(message = "player_id must not be null or blank!")
    private String playerId;
    @NotBlank(message = "spin_id must not be null or blank!")
    private String spinId;
    @NotBlank(message = "game_type must not be null or blank!")
    private String gameType;
    @NotBlank(message = "game_name must not be null or blank!")
    private String gameName;
    @PositiveOrZero(message = "bet_amount must be zero or a positive number!")
    private double betAmount;
    @PositiveOrZero(message = "payout must be zero or a positive number!")
    private double payout;
    @NotBlank(message = "created_at must not be null or blank!")
    private String createdAt;
}
