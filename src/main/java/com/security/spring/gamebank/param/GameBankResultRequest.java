package com.security.spring.gamebank.param;

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
