package com.security.spring.bank.bankName.entity;

//import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankType.entity.BankType;
//import com.security.spring.deposit.entity.Deposit;
//import com.security.spring.withdraw.entity.Withdraw;
import com.security.spring.deposit.entity.Deposit;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name="bank_name")
public class BankName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="bank_type_id")
    private BankType bankType;
    private String bankName;
    private String bankNameImageUrl;

    @OneToMany(mappedBy = "bankName" , cascade = CascadeType.ALL)
    private List<BankAcc> bankAccList;

//
//    @OneToMany(mappedBy = "bankName")
//    private List<Withdraw> withdrawList;
}
