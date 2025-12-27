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
@Table(
        name = "commission_confirm",
        indexes = {
                @Index(name = "idx_commission_confirm_date", columnList = "confirm_date"),
                @Index(name = "idx_commission_agent_date", columnList = "agent_ar7_id,confirm_date"),
                @Index(name = "idx_commission_master_date", columnList = "master_ar7_id,confirm_date"),
                @Index(name = "idx_commission_semaster_date", columnList = "se_master_a7_id,confirm_date")
        }
)
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
