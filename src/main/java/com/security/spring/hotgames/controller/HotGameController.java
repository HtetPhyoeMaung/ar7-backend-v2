package com.security.spring.hotgames.controller;

import com.security.spring.hotgames.service.HotGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ggl/hot-games")
public class HotGameController {
    private final HotGameService hotGameService;

    @GetMapping
    public ResponseEntity<String> getHotGames(){
        String hotGames = hotGameService.getHotGames();
        return ResponseEntity.ok(hotGames);
    }
}
