package com.security.spring.dashboard;

import com.security.spring.dashboard.dto.DashboardResponse;
import com.security.spring.dashboard.dto.GameRequest;
import com.security.spring.spacetechmm.service.SpaceTechService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard/spacetech/game")
@RequiredArgsConstructor
public class SpaceTechDashboard {

    private final SpaceTechService spaceTechService;

    @PostMapping
    public ResponseEntity<DashboardResponse> createGame(@RequestParam(name = "gameCode") String gameCode,
                                                        @RequestParam(name = "gameName") String gameName,
                                                        @RequestParam(name = "gameType") int gameType,
                                                        @RequestParam(name = "image")MultipartFile image) throws IOException {
        GameRequest gameRequest = GameRequest.builder()
                .gameCode(gameCode)
                .gameName(gameName)
                .gameType(gameType)
                .image(image)
                .build();
       return spaceTechService.createGame(gameRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DashboardResponse> updateGame(
                                                        @PathVariable(name = "id") int gameId,
                                                        @RequestParam(name = "gameName") String gameName,
                                                        @RequestParam(name = "gameType") int gameType,
                                                        @RequestParam(name = "image", required = false)MultipartFile image) throws IOException {
        GameRequest gameRequest = GameRequest.builder()
                .gameName(gameName)
                .gameType(gameType)
                .image(image)
                .build();

        return spaceTechService.updateGame(gameRequest,gameId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardResponse> findById(@PathVariable(name = "id") int gameId){
        return spaceTechService.findById(gameId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DashboardResponse> deleteById(@PathVariable(name = "id") int gameId){
        return spaceTechService.deleteById(gameId);
    }




}
