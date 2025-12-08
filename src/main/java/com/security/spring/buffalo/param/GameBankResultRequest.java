package com.security.spring.buffalo.param;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
public class GameBankResultRequest {
    private String agentCode;
    private String userId;
    private String spinId;
    private double betAmount;
    private double payout;
    private String createdAt;

}
