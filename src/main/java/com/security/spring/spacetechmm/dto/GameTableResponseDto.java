package com.security.spring.spacetechmm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameTableResponseDto {

    private Long spaceTechId;
    private String message;
    private String stateCode;
    private GameTableResponse gameTableResponse;
    private List<GameTableResponse> gameTableResponseList;


}
