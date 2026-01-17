package com.security.spring.bank.bankName.entity;

import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="bank_name_auth", indexes = {
        @Index(name = "idx_bank_name_auth_user", columnList = "user_id"),
        @Index(name = "idx_bank_name_auth_bank_type", columnList = "bank_type_id"),
        @Index(name = "idx_bank_name_auth_bank_name", columnList = "bank_name_id"),
        @Index(name = "idx_bank_name_auth_status", columnList = "bankNameStatus"),
        @Index(name = "idx_bank_name_auth_initial_status", columnList = "initialStatus"),
        @Index(name = "idx_bank_name_auth_user_bank_name", columnList = "user_id,bank_name_id"),
        @Index(name = "idx_bank_name_auth_user_status", columnList = "user_id,bankNameStatus"),
        @Index(name = "idx_bank_name_auth_user_type_name", columnList = "user_id,bank_type_id,bank_name_id"),
        @Index(name = "idx_bank_name_auth_user_status_initial", columnList = "user_id,bankNameStatus,initialStatus")
})
public class BankNameAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User ownerUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_type_id")
    @NotNull
    private BankType bankType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_name_id")
    @NotNull
    private BankName bankName;
    @NotNull
    private Boolean bankNameStatus;
    private Integer initialStatus;
}
