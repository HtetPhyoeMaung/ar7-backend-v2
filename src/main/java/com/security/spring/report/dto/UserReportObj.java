package com.security.spring.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReportObj {
    private String userId;
    private String gameTypeName;
    private int gameTypeId;
    private int totalBetCount;
    private int totalBetAmount;
    private int totalWinAmount;
    private int winLoseAmount;



}
