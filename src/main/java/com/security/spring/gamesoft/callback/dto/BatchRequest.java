package com.security.spring.gamesoft.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchRequest {
    @JsonProperty("member_account")
    private String memberAccount;
    @JsonProperty("product_code")
    private int productCode;
    @JsonProperty("game_type")
    private String gameType;
    @JsonProperty("transactions")
    private List<CallBackTransaction> transactions;
}
