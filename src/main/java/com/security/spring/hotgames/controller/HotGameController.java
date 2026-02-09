package com.security.spring.hotgames.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.spring.hotgames.dto.AddHotGameRequest;
import com.security.spring.hotgames.entity.HotGameItem;
import com.security.spring.hotgames.service.HotGameService;
import com.security.spring.thirdpartygames.getGameList.dto.ProviderGame;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ggl/hot-games")
@Tag(name = "Hot Games", description = "APIs for managing and retrieving hot games")
public class HotGameController {
    private final HotGameService hotGameService;

    @GetMapping
    public ResponseEntity<Map<String, List<ProviderGame>>> getHotGames(){
        return ResponseEntity.ok(hotGameService.getHotGames());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<HotGameItem>> getHotGamesForDashboard(){
        return ResponseEntity.ok(hotGameService.getHotGameItems());
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshHotGames() {
        hotGameService.refreshHotGames();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/add")
    public ResponseEntity<Void> addHotGame(@Valid @RequestBody AddHotGameRequest request) {
        hotGameService.addHotGame(request);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeHotGame(@PathVariable Integer id) {
        hotGameService.removeHotGame(id);
        return ResponseEntity.ok().build();
    }
}