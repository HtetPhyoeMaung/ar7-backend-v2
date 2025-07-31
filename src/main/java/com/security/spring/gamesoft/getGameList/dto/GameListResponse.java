package com.security.spring.gamesoft.getGameList.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameListResponse {

    @JsonProperty("provider_games")
    private List<ProviderGame> providerGames;
    @JsonProperty("code")
    private int errorCode;
    @JsonProperty("message")
    private String errorMessage;
    @JsonProperty("pagination")
    private Pagination pagination;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Pagination{
     private int size;
     private int total;
     private int offset;
    }

}


