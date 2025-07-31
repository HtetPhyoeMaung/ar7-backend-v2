package com.security.spring.deposit.entity;

import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="deposit")
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
