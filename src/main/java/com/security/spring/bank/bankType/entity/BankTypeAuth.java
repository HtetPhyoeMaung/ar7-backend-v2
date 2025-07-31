package com.security.spring.bank.bankType.entity;

import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banktype_auth")
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
