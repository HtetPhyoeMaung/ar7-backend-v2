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
public class CommissionConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String agentAr7Id;
    private double agentWinLoseAmount;
    private String masterAr7Id;
    private double masterWinLoseAmount;
    private String seMasterA7Id;
    private double seMasterWinLoseAmount;
    private LocalDateTime confirmDate;
}
