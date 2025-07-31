package com.security.spring.gamesoft.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBalanceCallBackResponse {
    @JsonProperty("member_account")
    private String memberAccount;
    @JsonProperty("product_code")
    private int productCode;
    @JsonProperty("balance")
    private long balance;
    private int code;
    private String message;
}
