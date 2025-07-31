package com.security.spring.gamesoft.getGameList.rest;

import com.security.spring.config.JWTService;
import com.security.spring.gamesoft.getGameList.dto.GetGameListRequest;
import com.security.spring.gamesoft.getGameList.dto.GetGameListResponse;
import com.security.spring.gamesoft.getGameList.service.GetGameListService;
import com.security.spring.spacetechmm.dto.SpaceTechGameDto;
import com.security.spring.spacetechmm.service.SpaceTechService;
import com.security.spring.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ggl")
@RequiredArgsConstructor
public class GetGameListController {


    private final JWTService jwtService;

    private final SpaceTechService spaceTechService;

    private final GetGameListService getGameListService;

    @PostMapping("/getgamelist")
    public ResponseEntity<GetGameListResponse> getGameList(@RequestBody GetGameListRequest data) {
        String ar7Id = ContextUtils.getAr7IdFromContext();

        GetGameListResponse response = getGameListService.getGameListConfig(data, ar7Id);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-spacetech-game-list")
    public ResponseEntity<GetGameListResponse> getSpaceTechGameList(@RequestParam(name = "gameTypeId") Long gameTypeId){
        List<SpaceTechGameDto> spaceTechGameDtoList = spaceTechService.getGameList(gameTypeId);
        GetGameListResponse getGameListResponse = GetGameListResponse.builder()
                .spaceTechGameDtoList(spaceTechGameDtoList)
                .productName("SpaceTech")
                .build();
        return ResponseEntity.ok(getGameListResponse);
    }
    @GetMapping("/card-games/{gameTypeId}/screen")
    public ResponseEntity<GetGameListResponse> getScreenRatioForCardGames(@RequestParam("screenId") Integer screenId,
                                                                          @PathVariable Long gameTypeId
                                                                          ){
        List<SpaceTechGameDto> spaceTechGameDtoList = spaceTechService.getCardsGameByScreenId(screenId,gameTypeId);
        GetGameListResponse getGameListResponse = GetGameListResponse.builder()
                .spaceTechGameDtoList(spaceTechGameDtoList)
                .productName("SpaceTech")
                .build();
        return ResponseEntity.ok(getGameListResponse);
    }
}
