package com.security.spring.gamesoft.lunchGame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LunchGameRequest {
    @JsonProperty("operator_code")
    private String operatorCode;

    @JsonProperty("member_account")
    private String memberName;

    @JsonProperty("nickname")
    private String displayName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("game_code")
    private String gameID;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("product_code")
    private Integer productID;

    @JsonProperty("game_type")
    private String  gameType;

    @JsonProperty("language_code")
    private String  languageCode;

    @JsonProperty("platform")
    private Platform  platform;

    @JsonProperty("ip")
    private String iPAddress;

    @JsonProperty("operator_lobby_url")
    private String operatorLobbyURL;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("request_time")
    private String requestTime;
}
