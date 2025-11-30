package com.security.spring.buffalo.param;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BuffaloBalanceParam {
    @NotBlank(message = "agent_code must not be null or blank!")
    private String agentCode;
    @NotBlank(message = "password must not be null or blank!")
    private String password;
    @NotBlank(message = "player_id must not be null or blank!")
    private String playerId;
}
