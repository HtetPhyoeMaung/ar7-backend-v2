package com.security.spring.gamesoft.gameType.service;

import com.security.spring.exceptionall.DataAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamesoft.gameType.dto.GameTypeObj;
import com.security.spring.gamesoft.gameType.dto.GameTypeRequest;
import com.security.spring.gamesoft.gameType.dto.GameTypeResponse;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameType.repo.GameTypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameTypeServiceImpl implements GameTypeService{

    private final GameTypeRepo gameTypeRepo;

    @Override
    @Transactional
    public GameTypeResponse saveGameType(GameTypeRequest data) {
        Optional<GameType> resGameType = gameTypeRepo.findByCode(data.getCode());
        if(resGameType.isPresent()){
            throw new DataAlreadyExistException("This code already exist");
        }
        GameType saveObj = GameType
                .builder()
                .code(data.getCode())
                .description(data.getDescription())
                .build();

        GameType resSaveObj = gameTypeRepo.save(saveObj);

        return GameTypeResponse
                .builder()
                .gameTypeObjList(null)
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Create " + resSaveObj.getDescription() + "Game Type")
                .build();
    }

    @Override
    @Transactional
    public GameTypeResponse getAllGameType() {
        List<GameType> gameTypeList = gameTypeRepo.findAll();

        List<GameTypeObj> gameTypeObjList = gameTypeList
                .stream()
                .map(
                        obj -> GameTypeObj
                                .builder()
                                .code(obj.getCode())
                                .description(obj.getDescription())
                                .id(obj.getId())
                                .build()
                )
                .toList();


        return GameTypeResponse
                .builder()
                .message("Game Type All")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .gameTypeObjList(gameTypeObjList)
                .build();
    }

    @Override
    @Transactional
    public GameTypeResponse updateGameType(GameTypeRequest data) {
        Optional<GameType> gameType = gameTypeRepo.findById(data.getId());
        if(gameType.isEmpty()){
            throw new DataNotFoundException("Game Type Not Found");
        }
        GameType gameTypeGet = gameType.get();

        Optional.ofNullable(data.getCode())
                .ifPresent(gameTypeGet::setCode);

        Optional.ofNullable(data.getDescription())
                .ifPresent(gameTypeGet::setDescription);

        GameType returnGameType = gameTypeRepo.save(gameTypeGet);

        return GameTypeResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("Already Update")
                .build();
    }


    @Override
    public GameTypeResponse getGameTypeResponseById(int id) {
        Optional<GameType> gameType =gameTypeRepo.findById(id);
        List<GameTypeObj> gameTypeObjList = gameType
                .stream()
                .map( obj -> GameTypeObj
                        .builder()
                        .code(obj.getCode())
                        .description(obj.getDescription())
                        .id(obj.getId())
                        .build())
                .collect(Collectors.toList());

        return GameTypeResponse
                .builder()
                .message("Game Type All")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .gameTypeObjList(gameTypeObjList)
                .build();
    }


    @Override
    public GameType findByCode(String  gameType) {
        return gameTypeRepo.findByCode(gameType).orElseThrow(()->
                new DataNotFoundException("GameType not found by "+gameType));
    }

    @Override
    public GameTypeResponse deleteGameTypeById(int id) {
        gameTypeRepo.deleteById(id);
        return GameTypeResponse.builder()
                .status(true)
                .statusCode(200)
                .message("Successfully deleted by ID : "+id)
                .build();
    }

    @Override
    public GameTypeResponse getAllSpaceTechGameTypes() {
        List<GameType> gameTypeList = gameTypeRepo.findAll();
        List<GameType> spaceTechGameTypeList = gameTypeList.stream().filter(gt->
                gt.getCode().equals("17")||gt.getCode().equals("18")||gt.getCode().equals("19")
                ).toList();

        List<GameTypeObj> gameTypeObjList =spaceTechGameTypeList.stream()
                .map(
                        obj -> GameTypeObj
                                .builder()
                                .code(obj.getCode())
                                .description(obj.getDescription())
                                .id(obj.getId())
                                .build()
                )
                .toList();

        return GameTypeResponse
                .builder()
                .message("Game Type All")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .gameTypeObjList(gameTypeObjList)
                .build();
    }

    @Override
    public GameType findById(int gameType) {
        return gameTypeRepo.findById(gameType).orElseThrow(()-> new DataNotFoundException("Game Type not found by ID :"+gameType));
    }


}
