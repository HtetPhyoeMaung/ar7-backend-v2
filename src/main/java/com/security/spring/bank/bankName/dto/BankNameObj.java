package com.security.spring.bank.bankName.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankNameObj {
    private Integer id;
    private Integer bankTypeId;
    private String bankTypeName;
    private String bankName;
    private String bankLogoUrl;
}
