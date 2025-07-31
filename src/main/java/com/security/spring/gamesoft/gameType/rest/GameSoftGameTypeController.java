package com.security.spring.gamesoft.gameType.rest;

import com.security.spring.gamesoft.gameType.dto.GameTypeRequest;
import com.security.spring.gamesoft.gameType.dto.GameTypeResponse;
import com.security.spring.gamesoft.gameType.service.GameTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gt")
@RequiredArgsConstructor
public class GameSoftGameTypeController {

    @Autowired
    private GameTypeService gameTypeServiceImpl;

    @PostMapping("/gametype")
    public ResponseEntity<GameTypeResponse> saveGameType(@RequestBody @Valid GameTypeRequest gameType){
        GameTypeResponse resObj = gameTypeServiceImpl.saveGameType(gameType);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/gametype/{id}")
    public ResponseEntity<GameTypeResponse> getGameTypeById(@PathVariable int id){
        GameTypeResponse resObj = gameTypeServiceImpl.getGameTypeResponseById(id);
        return ResponseEntity.ok().body(resObj);
    }
    @DeleteMapping("/gametype/{id}")
    public ResponseEntity<GameTypeResponse> deleteGameTypeById(@PathVariable(name = "id") int id){
        GameTypeResponse gameTypeResponse = gameTypeServiceImpl.deleteGameTypeById(id);
        return ResponseEntity.ok(gameTypeResponse);
    }

    @GetMapping("/gametype")
    public ResponseEntity<?> getAllGameType(){
        GameTypeResponse resObj = gameTypeServiceImpl.getAllGameType();
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping("/gametype")
    public ResponseEntity<?> updateGameType(@RequestBody GameTypeRequest data){
        GameTypeResponse resObj = gameTypeServiceImpl.updateGameType(data);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/space-tech/game-type")
    public ResponseEntity<GameTypeResponse> getAllSpaceTechGameTypes(){
        GameTypeResponse gameTypeResponse = gameTypeServiceImpl.getAllSpaceTechGameTypes();
        return ResponseEntity.ok(gameTypeResponse);
    }
}
