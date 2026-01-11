package com.security.spring.deposit.entity;

import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="deposit", indexes = {
        @Index(name = "idx_deposit_from_acc", columnList = "from_acc_id"),
        @Index(name = "idx_deposit_to_acc", columnList = "to_acc_id"),
        @Index(name = "idx_deposit_status", columnList = "status"),
        @Index(name = "idx_deposit_transfer_time", columnList = "transferTime"),
        @Index(name = "idx_deposit_action_time", columnList = "actionTime"),
        @Index(name = "idx_deposit_user_transition_id", columnList = "userTransitionId"),
        @Index(name = "idx_deposit_to_acc_status", columnList = "to_acc_id,status"),
        @Index(name = "idx_deposit_from_acc_status", columnList = "from_acc_id,status"),
        @Index(name = "idx_deposit_transfer_time_to_acc", columnList = "transferTime,to_acc_id"),
        @Index(name = "idx_deposit_transfer_time_from_acc", columnList = "transferTime,from_acc_id")
})
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    @ManyToOne
    @JoinColumn(name = "from_acc_id")
    @ToString.Exclude
    private User fromAcc;
    @ManyToOne
    @JoinColumn(name = "to_acc_id")
    @ToString.Exclude
    private User toAcc;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountName;

    private String remark;
    @Enumerated(EnumType.STRING)
    private DepositStatus status;

    private LocalDateTime transferTime;
    private String userTransitionId;
    private LocalDateTime actionTime;
}
