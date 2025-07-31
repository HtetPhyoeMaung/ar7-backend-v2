package com.security.spring.spacetechmm.rest;

import com.security.spring.spacetechmm.dto.GameTableRequest;
import com.security.spring.spacetechmm.dto.GameTableResponseDto;
import com.security.spring.spacetechmm.service.GameTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/game-table")
@RequiredArgsConstructor
public class GameTableController {

    private final GameTableService gameTableService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameTableResponseDto> createGameTable(@RequestParam(name = "miniBet") Integer miniBet,
                                                                @RequestParam(name = "maxBet") Integer maxBet,
                                                                @RequestParam(name = "bet") Integer bet,
                                                                @RequestParam(name = "level") Integer level,
                                                                @RequestParam(name = "spaceTechId") Long spaceTechId,
                                                                @RequestParam(name = "image")MultipartFile image) throws IOException {
        GameTableRequest request = GameTableRequest.builder()
                .miniBet(miniBet)
                .maxBet(maxBet)
                .bet(bet)
                .level(level)
                .spaceTechId(spaceTechId)
                .image(image)
                .build();
        GameTableResponseDto response = gameTableService.createGameTable(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<GameTableResponseDto> getAllGameTable(@RequestParam(value = "spaceTechGameId") Long spaceTechGameId) {
        GameTableResponseDto gameTableResponse = gameTableService.getAllGameTable(spaceTechGameId);
        return ResponseEntity.ok(gameTableResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameTableResponseDto> getGameTableId(@PathVariable Long id) {
        GameTableResponseDto response = gameTableService.getGameTableId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameTableResponseDto> updateGameTable(@RequestParam(name = "miniBet") Integer miniBet,
                                                                @RequestParam(name = "maxBet") Integer maxBet,
                                                                @RequestParam(name = "bet") Integer bet,
                                                                @RequestParam(name = "level") Integer level,
                                                                @RequestParam(name = "spaceTechId") Long spaceTechId,
                                                                @RequestParam(name = "image", required = false)MultipartFile image,
                                                                @PathVariable(name = "id") Long id) throws IOException {
        GameTableRequest request = GameTableRequest.builder()
                .miniBet(miniBet)
                .maxBet(maxBet)
                .bet(bet)
                .level(level)
                .spaceTechId(spaceTechId)
                .build();
        if (image!=null){
            request.setImage(image);
        }
        GameTableResponseDto response = gameTableService.updateGameTable(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameTableResponseDto> deleteGameTable(@PathVariable Long id) {
        GameTableResponseDto response = gameTableService.deleteGameTable(id);
        return ResponseEntity.ok(response);
    }
}