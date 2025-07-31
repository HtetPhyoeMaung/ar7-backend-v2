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
@Table(name="withdraw")
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
