package com.security.spring.gamesoft.getGameList.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetGameListRequest {
    @JsonProperty("operator_code")
    private String operatorCode;
    @JsonProperty("product_code")
    private int productID;
    @JsonProperty("game_type")
    private String  gameType;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("request_time")
    private long requestTime;

}
