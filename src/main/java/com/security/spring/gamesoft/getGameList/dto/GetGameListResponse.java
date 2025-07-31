package com.security.spring.gamesoft.getGameList.dto;

import com.security.spring.spacetechmm.dto.SpaceTechGameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetGameListResponse {
    private GameListResponse gameListResponse;
    private List<SpaceTechGameDto> spaceTechGameDtoList;
    private String productName;
}
