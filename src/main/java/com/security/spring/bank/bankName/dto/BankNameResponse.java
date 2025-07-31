package com.security.spring.bank.bankName.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankNameResponse {
    private Integer statusCode;
    private String message;
    private Boolean status;
    private List<BankNameObj> bankNameObjList;
}
