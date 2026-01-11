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
        @Index(name = "idx_prefix_commission_agent_ar7_id", columnList = "agentAr7Id"),
        @Index(name = "idx_prefix_commission_master_ar7_id", columnList = "masterAr7Id"),
        @Index(name = "idx_prefix_commission_se_master_ar7_id", columnList = "seMasterAr7Id"),
        @Index(name = "idx_prefix_commission_agent_master_se", columnList = "agentAr7Id,masterAr7Id,seMasterAr7Id")
})
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
