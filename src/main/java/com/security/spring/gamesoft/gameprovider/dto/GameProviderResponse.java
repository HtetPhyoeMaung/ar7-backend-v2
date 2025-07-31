package com.security.spring.gamesoft.gameprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameProviderResponse {
    private String message;
    private Integer statusCode;
    private Boolean status;
    private List<GameProviderObj> gameProviderObjList;
}
