package com.security.spring.gamesoft.gameType.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameTypeResponse {

    private String message;
    private Integer statusCode;
    private Boolean status;
    private List<GameTypeObj> gameTypeObjList;
}
