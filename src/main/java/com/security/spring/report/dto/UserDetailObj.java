package com.security.spring.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailObj {
    private String  betTime;
    private String  resultTime;
    private String transactionId;
    private long wagerId;
    private String  gameCode;
    private String gameName;
    private String gameTypeCode;
    private double beforeBetAmount;
    private double betAmount;
    private double winAmount;
    private double transactionAmount;
    private double afterAmount;
    private String ar7Id;
    private String  status;
}
