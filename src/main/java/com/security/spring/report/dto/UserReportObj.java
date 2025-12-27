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
    private long totalBetCount;
    private double totalBetAmount;
    private double totalWinAmount;
    private double winLoseAmount;

    /**
     * JPQL projection constructor used by aggregate queries.
     */
    public UserReportObj(String userId,
                         Integer gameTypeId,
                         String gameTypeName,
                         Long totalBetCount,
                         Double totalBetAmount,
                         Double totalWinAmount) {
        this.userId = userId;
        this.gameTypeId = gameTypeId != null ? gameTypeId : 0;
        this.gameTypeName = gameTypeName;
        this.totalBetCount = totalBetCount != null ? totalBetCount : 0;
        this.totalBetAmount = totalBetAmount != null ? totalBetAmount : 0.0;
        this.totalWinAmount = totalWinAmount != null ? totalWinAmount : 0.0;
        this.winLoseAmount = this.totalWinAmount - this.totalBetAmount;
    }
}
