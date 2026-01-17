package com.security.spring.bank.bankType.entity;

import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banktype_auth", indexes = {
        @Index(name = "idx_bank_type_auth_user", columnList = "user_id"),
        @Index(name = "idx_bank_type_auth_bank_type", columnList = "bank_type_id"),
        @Index(name = "idx_bank_type_auth_status", columnList = "bankTypeStatus"),
        @Index(name = "idx_bank_type_auth_init_status", columnList = "initStatus"),
        @Index(name = "idx_bank_type_auth_user_bank_type", columnList = "user_id,bank_type_id"),
        @Index(name = "idx_bank_type_auth_user_status", columnList = "user_id,bankTypeStatus"),
        @Index(name = "idx_bank_type_auth_user_status_init", columnList = "user_id,bankTypeStatus,initStatus")
})
public class BankTypeAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User ownerUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="bank_type_id")
    private BankType bankType;

    private boolean bankTypeStatus;
    private Integer initStatus;
}
