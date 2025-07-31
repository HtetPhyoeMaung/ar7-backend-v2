package com.security.spring.bank.bankName.dto;

import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankNameAuthResponse {
    private String message;
    private Integer statusCode;
    private Boolean status;
    private List<BankNameAuthObj> bankNameAuthObjList;
}
