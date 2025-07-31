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
public class PrefixCommission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String agentAr7Id;
    private double totalDownLine;
    private double totalBetAmount;
    private double agentWinLoseAmount;
    private double masterWinLoseAmount;
    private double seMasterWinLoseAmount;
    private String masterAr7Id;
    private String seMasterAr7Id;
    private LocalDateTime comfirmDate;
}
