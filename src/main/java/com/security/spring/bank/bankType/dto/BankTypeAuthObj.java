package com.security.spring.bank.bankType.dto;

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
public class BankTypeAuthObj {
    private Integer id;
    private String parentUserName;
    private String availableBankTypeName;
    private Integer availableBankTypeId;
    private Boolean bankTypeStatus;
    private Integer initialStatus;
}
