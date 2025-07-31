package com.security.spring.bank.bankAcc.entity;

import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.deposit.entity.Deposit;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name="bank_acc")
public class BankAcc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="bankNameId")
    @ToString.Exclude
    private BankName bankName;

    @Column(name="account_name")
    private String accountName;

    @Column(name="account_number")
    private String accountNumber;

    @Column(name = "qr_img_url")
    private String qrImgUrl;

    @Column(name="description")
    private String description;

    @Column(name="account_status")
    private boolean accountStatus;

    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User ownerUser;

}
