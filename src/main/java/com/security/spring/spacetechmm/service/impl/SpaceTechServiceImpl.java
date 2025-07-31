package com.security.spring.spacetechmm.service.impl;

import com.security.spring.dashboard.dto.DashboardResponse;
import com.security.spring.dashboard.dto.GameRequest;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.gamesoft.gameType.service.GameTypeService;
import com.security.spring.spacetechmm.dto.SpaceTechGameDto;
import com.security.spring.spacetechmm.entity.SpaceTechGame;
import com.security.spring.spacetechmm.repo.SpaceTechRepository;
import com.security.spring.spacetechmm.service.SpaceTechService;
import com.security.spring.storage.StorageService;
import com.security.spring.utils.Constraint;
import com.security.spring.utils.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceTechServiceImpl implements SpaceTechService {

    private final SpaceTechRepository spaceTechRepository;
    private final GameTypeService gameTypeService;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    public List<SpaceTechGameDto> getGameList(Long gameTypeId){
        return spaceTechRepository.findAllByGameType_Id(gameTypeId).stream().map(objectMapper::mapToSpaceTechGameDto).toList();
    }

    @Override
    public ResponseEntity<DashboardResponse> createGame(GameRequest gameRequest) throws IOException {

       if (existsByGameNameAndGameTypeId(gameRequest.getGameName(),gameRequest.getGameType())){
           throw new CustomAlreadyExistException("This Game is already exists!");
       }
        SpaceTechGame spaceTechGame = SpaceTechGame.builder()
                .gameCode(gameRequest.getGameCode())
                .gameName(gameRequest.getGameName())
                .gameType(gameTypeService.findById(gameRequest.getGameType()))
                .build();
       if (gameRequest.getImage()!=null){
           spaceTechGame.setImageName(storageService.uploadImage(gameRequest.getImage()));
       }
       spaceTechGame = spaceTechRepository.save(spaceTechGame);
       SpaceTechGameDto spaceTechGameDto = objectMapper.mapToSpaceTechGameDto(spaceTechGame);
       DashboardResponse dashboardResponse = DashboardResponse.builder()
               .spaceTechGameDto(spaceTechGameDto)
               .status(201)
               .message(Constraint.CREATED_SUCCESS_MESSAGE)
               .build();
       return ResponseEntity.ok(dashboardResponse);
    }

    @Override
    public List<SpaceTechGameDto> getCardsGameByScreenId(Integer screenId, Long gameTypeId) {
        List<SpaceTechGame> spaceTechGameList =  spaceTechRepository.findAllByGameType_Id(gameTypeId);
        if (spaceTechGameList.isEmpty()){
            throw new DataNotFoundException("There is no cards game with this game type id : "+gameTypeId);
        }
        if (screenId.equals(0)){
           spaceTechGameList=spaceTechGameList.stream().filter(obj->!obj.getGameName().contains("landscape")).toList();
        } else if (screenId.equals(1)) {
            spaceTechGameList=spaceTechGameList.stream().filter(obj->obj.getGameName().contains("landscape")).toList();
        }else{
            throw new UnauthorizedException("Screen Id must be either '0' or '1'.");
        }
    return spaceTechGameList.stream().map(objectMapper::mapToSpaceTechGameDto).toList();


    }

    @Override
    public ResponseEntity<DashboardResponse> updateGame(GameRequest gameRequest, long gameId) throws IOException {
        SpaceTechGame spaceTechGame = getSpaceTechGameById(gameId);
        spaceTechGame.setGameName(gameRequest.getGameName());
        spaceTechGame.setGameType(gameTypeService.findById(gameRequest.getGameType()));
        if (gameRequest.getImage()!=null) {
            if (spaceTechGame.getImageName()!=null) {
                spaceTechGame.setImageName(storageService.updateImage(gameRequest.getImage(), spaceTechGame.getImageName()));
            }
            spaceTechGame.setImageName(storageService.uploadImage(gameRequest.getImage()));
        }
        spaceTechGame = spaceTechRepository.save(spaceTechGame);
        SpaceTechGameDto spaceTechGameDto = objectMapper.mapToSpaceTechGameDto(spaceTechGame);
        DashboardResponse dashboardResponse = DashboardResponse.builder()
                .spaceTechGameDto(spaceTechGameDto)
                .status(200)
                .message(Constraint.UPDATED_SUCCESS_MESSAGE)
                .build();
        return ResponseEntity.ok(dashboardResponse);
    }

    @Override
    public ResponseEntity<DashboardResponse> findById(int gameId) {
        SpaceTechGame spaceTechGame = getSpaceTechGameById(gameId);
        SpaceTechGameDto spaceTechGameDto = objectMapper.mapToSpaceTechGameDto(spaceTechGame);
        DashboardResponse dashboardResponse = DashboardResponse.builder()
                .spaceTechGameDto(spaceTechGameDto)
                .status(200)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .build();
        return ResponseEntity.ok(dashboardResponse);
    }

    @Override
    public ResponseEntity<DashboardResponse> deleteById(int gameId) {
        SpaceTechGame spaceTechGame = getSpaceTechGameById(gameId);
        spaceTechRepository.deleteById(spaceTechGame.getId());
        DashboardResponse dashboardResponse = DashboardResponse.builder()
                .status(200)
                .message(Constraint.DELETED_SUCCESS_MESSAGE)
                .build();
        return ResponseEntity.ok(dashboardResponse);
    }

    private boolean existsByGameNameAndGameTypeId(String gameName, int  gameType) {
        return spaceTechRepository.existsByGameNameAndGameType_Id(gameName, gameType);
    }

    public SpaceTechGame getSpaceTechGameById(long gameId){
        return spaceTechRepository.findById(gameId).orElseThrow(()->
                new DataNotFoundException("SpaceTech Game Not Found By ID : "+gameId)
                );
    }



}
