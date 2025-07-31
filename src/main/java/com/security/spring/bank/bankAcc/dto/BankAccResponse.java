package com.security.spring.bank.bankAcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccResponse {
    private String message;
    private Boolean status;
    private Integer statusCode;
    private BankAccObj bankAccObj;
    private List<BankAccObj> bankAccObjList;
}
