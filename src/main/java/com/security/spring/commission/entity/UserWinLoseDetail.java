package com.security.spring.commission.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
