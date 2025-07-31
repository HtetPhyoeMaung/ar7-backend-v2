package com.security.spring.bank.bankType.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankTypeResponse {
    private String message;
    private int statusCode;
    private boolean status;
    private BankTypeObj bankTypeObj;
    private List<BankTypeObj> bankTypeObjList;
}
