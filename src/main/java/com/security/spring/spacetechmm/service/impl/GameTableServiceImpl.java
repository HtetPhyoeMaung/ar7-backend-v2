package com.security.spring.spacetechmm.service.impl;

import com.amazonaws.services.kms.model.NotFoundException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.spacetechmm.dto.GameTableRequest;
import com.security.spring.spacetechmm.dto.GameTableResponse;
import com.security.spring.spacetechmm.dto.GameTableResponseDto;
import com.security.spring.spacetechmm.entity.GameTable;
import com.security.spring.spacetechmm.entity.SpaceTechGame;
import com.security.spring.spacetechmm.repo.GameTableRepository;
import com.security.spring.spacetechmm.repo.SpaceTechRepository;
import com.security.spring.spacetechmm.service.GameTableService;
import com.security.spring.storage.StorageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameTableServiceImpl implements GameTableService {

    private final GameTableRepository gameTableRepository;
    private final SpaceTechRepository spaceTechGameRepository;
    private final StorageService storageService;

    @Override
    public GameTableResponseDto createGameTable(GameTableRequest request) throws IOException {
        SpaceTechGame spaceTechGameOpt = spaceTechGameRepository.findById(request.getSpaceTechId()).orElseThrow(()->
                new NotFoundException("SpaceTechGame  not found"));
         int gameCode = Integer.parseInt(spaceTechGameOpt.getGameType().getCode());
        if (gameCode==17){
            throw new UnauthorizedException("This game type not allowed to create level-tables");
        }

        validateMinBetAndMaxBet(request);
        GameTable gameTable = GameTable.builder()
                .miniBet(request.getMiniBet())
                .maxBet(request.getMaxBet())
                .level(request.getLevel())
                .bet(request.getBet())
                .spaceTechGame(spaceTechGameOpt)
                .createDate(LocalDateTime.now())
                .imageName(storageService.uploadImage(request.getImage()))
                .build();

        GameTable savedGameTable = gameTableRepository.save(gameTable);

        return GameTableResponseDto.builder()
                .spaceTechId(request.getSpaceTechId())
                .message("Game table created successfully")
                .stateCode("201")
                .gameTableResponse(convertToResponse(savedGameTable))
                .build();
    }

    private void validateMinBetAndMaxBet(GameTableRequest request) {
        if (request.getLevel()>1){
            GameTable gameTable = gameTableRepository.findByLevelAndSpaceTechGame_Id(request.getLevel()-1,request.getSpaceTechId()).orElseThrow(()->
                    new UnauthorizedException("Please create first "+(request.getLevel()-1)+" level table."));
            if (gameTable.getMiniBet()>request.getMiniBet() || gameTable.getMaxBet()>request.getMaxBet()){
                throw new UnauthorizedException("This min-bet and max-bet must be greater than "+gameTable.getLevel()+" level table");
            }
        }
    }

    @Override
    public GameTableResponseDto getAllGameTable(Long spaceTechGameId) {
        List<GameTable> gameTables = gameTableRepository.findAllBySpaceTechGame_Id(spaceTechGameId);

        if (gameTables.isEmpty()) {
            return GameTableResponseDto.builder()
                    .message("No game tables found")
                    .stateCode("200")
                    .build();
        }

        List<GameTableResponse> responseList = gameTables.stream()
                .map(this::convertToResponse)
                .toList();

        return GameTableResponseDto.builder()
                .message("Game tables retrieved successfully")
                .stateCode("200")
                .gameTableResponseList(responseList)
                .build();
    }

    @Override
    public GameTableResponseDto getGameTableId(Long id) {
        Optional<GameTable> gameTableOpt = gameTableRepository.findById(id);

        if (gameTableOpt.isEmpty()) {
            return GameTableResponseDto.builder()
                    .message( "Game table not found with ID: " + id)
                    .stateCode("404")
                    .build();
        }

        GameTable gameTable = gameTableOpt.get();
        return GameTableResponseDto.builder()
                .spaceTechId(gameTable.getSpaceTechGame().getId())
                .message("Game table retrieved successfully")
                .stateCode("200")
                .gameTableResponse(convertToResponse(gameTable))
                .build();
    }

    @Override
    public GameTableResponseDto updateGameTable(Long id, GameTableRequest request) throws IOException {
        Optional<GameTable> gameTableOpt = gameTableRepository.findById(id);

        if (gameTableOpt.isEmpty()) {
            return GameTableResponseDto.builder()
                    .spaceTechId(request.getSpaceTechId())
                    .message("Game table not found with ID: " + id)
                    .stateCode("404")
                    .build();
        }

        SpaceTechGame spaceTechGameOpt = spaceTechGameRepository.findById(request.getSpaceTechId()).orElseThrow(()->
                new DataNotFoundException("Space Tech Game not found by ID : "+request.getSpaceTechId()));

        validateMinBetAndMaxBet(request);
        GameTable gameTable = gameTableOpt.get();
        gameTable.setMiniBet(request.getMiniBet());
        gameTable.setMaxBet(request.getMaxBet());
        gameTable.setLevel(request.getLevel());
        gameTable.setBet(request.getBet());
        gameTable.setSpaceTechGame(spaceTechGameOpt);
        gameTable.setUpDate(LocalDateTime.now());

        if (request.getImage()!=null){
            if (gameTable.getImageName()!=null) {
                gameTable.setImageName(storageService.updateImage(request.getImage(),gameTable.getImageName()));
            }
            gameTable.setImageName(storageService.uploadImage(request.getImage()));

        }

        GameTable updatedGameTable = gameTableRepository.save(gameTable);

        return GameTableResponseDto.builder()
                .spaceTechId(request.getSpaceTechId())
                .message("Game table updated successfully")
                .stateCode("200")
                .gameTableResponse(convertToResponse(updatedGameTable))
                .build();
    }

    @Override
    public GameTableResponseDto deleteGameTable(Long id) {
        Optional<GameTable> gameTableOpt = gameTableRepository.findById(id);

        if (gameTableOpt.isEmpty()) {
            return GameTableResponseDto.builder()
                    .message("Game table not found with ID: " + id)
                    .stateCode("404")
                    .build();
        }

        gameTableRepository.deleteById(id);

        return GameTableResponseDto.builder()
                .message("Game table deleted successfully")
                .stateCode("200")
                .build();
    }

    private GameTableResponse convertToResponse(GameTable gameTable) {
        return GameTableResponse.builder()
                .id(gameTable.getId())
                .miniBet(gameTable.getMiniBet())
                .maxBet(gameTable.getMaxBet())
                .level(gameTable.getLevel())
                .bet(gameTable.getBet())
                .spaceTechId(gameTable.getSpaceTechGame().getId())
                .imageURL(storageService.getImageByName(gameTable.getImageName()))
                .createDate(gameTable.getCreateDate())
                .upDate(gameTable.getUpDate())
                .build();
    }
}