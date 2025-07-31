package com.security.spring.bank.bankType.entity;

//import com.security.spring.bank.bankAcc.entity.BankAcc;
//import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankAcc.entity.BankAcc;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name="bank_type")
@ToString
public class BankType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotEmpty
    @NotNull
    @Column(name="bank_type_name")
    private String bankTypeName;

}


