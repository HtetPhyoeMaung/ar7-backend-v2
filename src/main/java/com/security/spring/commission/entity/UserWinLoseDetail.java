package com.security.spring.commission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_user_win_lose_parent_agent", columnList = "parentAgentId"),
        @Index(name = "idx_user_win_lose_agent_commission_status", columnList = "agentCommissionStatus"),
        @Index(name = "idx_user_win_lose_confirm", columnList = "confirm"),
        @Index(name = "idx_user_win_lose_status_confirm", columnList = "agentCommissionStatus,confirm")
})
public class UserWinLoseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ar7Id;
    private double totalBetAmount;
    private double totalWinAmount;
    private int transactionLines;
    private String gameCode;
    private String gameTypeCode;
    private String gameTypeName;
    private boolean confirm;
    private LocalDateTime calculateDate;
    private double agentCommissionPercentage;
    private double agentWinLose;
    private String parentAgentId;
    private String masterId;
    private boolean agentCommissionStatus;
}
