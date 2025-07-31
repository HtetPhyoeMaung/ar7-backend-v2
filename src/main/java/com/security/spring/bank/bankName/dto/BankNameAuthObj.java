package com.security.spring.bank.bankName.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankNameAuthObj {
    private Integer id;
    private Integer bankTypeId;
    private String bankTypeName;
    private Integer bankNameId;
    private String bankName;
    private boolean bankNameStatus;
    private Integer initialStatus;
    private String bankNameImageUrl;
}
