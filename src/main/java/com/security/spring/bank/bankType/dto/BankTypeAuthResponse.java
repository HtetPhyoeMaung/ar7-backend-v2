package com.security.spring.bank.bankType.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTypeAuthResponse {
    private String message;
    private Integer statusCode;
    private Boolean status;
    private BankTypeAuthObj bankTypeAuthObj;
    private List<BankTypeAuthObj> bankTypeAuthObjList;
}
