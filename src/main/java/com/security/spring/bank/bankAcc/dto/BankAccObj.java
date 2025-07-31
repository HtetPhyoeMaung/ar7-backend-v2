package com.security.spring.bank.bankAcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccObj {
    private Integer bankAccId;
    private String bankAccName;
    private String bankAccAccount;
    private String bankName;
    private Integer bankNameId;
    private String accQrUrl;
    private Boolean accountStatus;
    private String description;
}
