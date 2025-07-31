package com.security.spring.commission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentCommissionUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String agentId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalUserWinAmount;
    private double totalUserBetAmount;
    private String  gameCode;
    private double commissionPercentage;
    private double profit;
}
