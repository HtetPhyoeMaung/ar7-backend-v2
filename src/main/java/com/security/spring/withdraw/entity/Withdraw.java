package com.security.spring.withdraw.entity;

import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name="withdraw", indexes = {
        @Index(name = "idx_withdraw_user", columnList = "withdraw_user_id"),
        @Index(name = "idx_withdraw_parent_user", columnList = "withdraw_parent_user_id"),
        @Index(name = "idx_withdraw_status", columnList = "withdrawStatus"),
        @Index(name = "idx_withdraw_action_time", columnList = "actionTime"),
        @Index(name = "idx_withdraw_parent_status", columnList = "withdraw_parent_user_id,withdrawStatus"),
        @Index(name = "idx_withdraw_user_status", columnList = "withdraw_user_id,withdrawStatus"),
        @Index(name = "idx_withdraw_action_time_parent", columnList = "actionTime,withdraw_parent_user_id"),
        @Index(name = "idx_withdraw_action_time_user", columnList = "actionTime,withdraw_user_id")
})
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private double amount;
    private DepositStatus withdrawStatus;
    private String withdrawBankAcc;
    private String withdrawBankAccNumber;
    private String description;

    @ManyToOne
    @JoinColumn(name = "withdraw_user_id")
    @ToString.Exclude
    private User withdrawUser;

    @ManyToOne
    @JoinColumn(name="withdraw_parent_user_id")
    @ToString.Exclude
    private User parentUser;

    @ManyToOne
    @JoinColumn(name="withdraw_bank_name")
    @ToString.Exclude
    private BankName bankName;

    private String withdrawTransitionNumber;
    private String adminTransitionNumber;
    private LocalDateTime actionTime;
}
