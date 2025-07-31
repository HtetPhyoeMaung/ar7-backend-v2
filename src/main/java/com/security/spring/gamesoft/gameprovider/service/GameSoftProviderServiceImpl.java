package com.security.spring.gamesoft.gameprovider.service;

import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameType.repo.GameTypeRepo;
import com.security.spring.gamesoft.gameprovider.dto.GameProviderObj;
import com.security.spring.gamesoft.gameprovider.dto.GameProviderResponse;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.gameprovider.repository.GameProviderRepo;
import com.security.spring.rro.GameSoftProviderRequest;
import com.security.spring.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameSoftProviderServiceImpl implements GameSoftGameProviderService {


    private final GameProviderRepo gameProviderRepo;
    private final StorageService storageService;
    private final GameTypeRepo gameTypeRepo;

    @Override
    @Transactional
    public GameProviderResponse saveGameProvider(GameSoftProviderRequest data) throws IOException {

        var gameType = gameTypeRepo.findByCode(data.getGameTypeCode()).orElseThrow(() ->
                new DataNotFoundException("Game Type Not Found By " + data.getGameTypeCode()));
        GameSoftGameProvider saveObj = GameSoftGameProvider
                .builder()
                .product(data.getProduct())
                .productCode(data.getProductCode())
                .imageName(storageService.uploadImage(data.getImage()))
                .currencyCode(data.getCurrencyCode())
                .conversionRate(data.getConversionRate())
                .gameType(gameType)
                .build();

        gameProviderRepo.save(saveObj);

        return GameProviderResponse
                .builder()
                .message("Save Success Game Provider")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public GameProviderResponse getAllGameProvider() {
        List<GameSoftGameProvider> gameProviderList = gameProviderRepo.findAll();
        List<GameProviderObj> gameProviderObjList = gameProviderList
                .stream()
                .map(
                        obj -> GameProviderObj
                                .builder()
                                .id(obj.getId())
                                .product(obj.getProduct())
                                .productCode(obj.getProductCode())
                                .gameTypeId(obj.getGameType().getId())
                                .imageUrl(obj.getImageName()==null?null:storageService
                                        .getImageByName(obj.getImageName()))
                                .gameTypeName(obj.getGameType().getDescription())
                                .currencyCode(obj.getCurrencyCode())
                                .conversionRate(obj.getConversionRate())
                                .build()
                )
                .toList();

        return GameProviderResponse
                .builder()
                .status(true)
                .message("API Good Working")
                .statusCode(HttpStatus.OK.value())
                .gameProviderObjList(gameProviderObjList)
                .build();
    }

    @Override
    @Transactional
    public GameProviderResponse providerFindById(Integer providerId) {
        Optional<GameSoftGameProvider> gameProvider = gameProviderRepo.findById(providerId);
        if (gameProvider.isEmpty()) {
            throw new DataNotFoundException("Game Provider Not Found");
        }

        List<GameProviderObj> gameProviderObjList = gameProvider
                .stream()
                .map(obj -> GameProviderObj
                        .builder()
                        .id(obj.getId())
                        .product(obj.getProduct())
                        .productCode(obj.getProductCode())
                        .gameTypeId(obj.getGameType().getId())
                        .imageUrl(obj.getImageName()==null?null:storageService
                                .getImageByName(obj.getImageName()))
                        .gameTypeName(obj.getGameType().getDescription())
                        .currencyCode(obj.getCurrencyCode())
                        .conversionRate(obj.getConversionRate())
                        .build())
                .toList();

        return GameProviderResponse
                .builder()
                .status(true)
                .message("API Good Working")
                .statusCode(HttpStatus.OK.value())
                .gameProviderObjList(gameProviderObjList)
                .build();
    }

    @Override
    public GameProviderResponse updateGameProvider(int id,GameSoftProviderRequest data) throws IOException {
        GameType gameType = null;
        if(data.getGameTypeCode() != null){
            gameType = gameTypeRepo.findByCode(data.getGameTypeCode()).orElseThrow(()->
                    new DataNotFoundException("Game Type Not Found"));
        }

        GameSoftGameProvider updateObj = gameProviderRepo.findById(id).orElseThrow(()->
              new DataNotFoundException("Game Provider Not Found"));
        updateObj.setProduct(data.getProduct() != null ? data.getProduct() : updateObj.getProduct());
        updateObj.setProductCode(data.getProductCode() != null ? data.getProductCode() : updateObj.getProductCode());
        updateObj.setCurrencyCode(data.getCurrencyCode() != null ? data.getCurrencyCode() : updateObj.getCurrencyCode());
        updateObj.setConversionRate( updateObj.getConversionRate());
        updateObj.setGameType(gameType != null ? gameType : updateObj.getGameType());
        if (data.getImage()!=null){
            updateObj.setImageName(storageService.updateImage(data.getImage(),updateObj.getImageName()));
        }
        gameProviderRepo.save(updateObj);

        return GameProviderResponse
                .builder()
                .message("Update Successfully")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .build();
    }

    @Override
    public GameSoftGameProvider findByProductAndGameType(Long productID, GameType gameType) {
        Optional<GameSoftGameProvider> gameProvider = gameProviderRepo.findByProductAndGameType(productID, gameType);
        if (gameProvider.isEmpty()) {
            throw new DataNotFoundException("GameProvider Not Found By " + productID + "and "+ gameType.getCode());
        }

         return gameProvider.get();
    }

    @Override
    @Transactional
    public GameProviderResponse getGameProviderByGameType(Integer gameTypeId) {
        Optional<GameType> gameType = gameTypeRepo.findById(gameTypeId);
        if(gameType.isEmpty()){
            throw new DataNotFoundException("Game Type Id Wrong");
        }
        GameType gameTypeGet = gameType.get();
        List<GameSoftGameProvider> gameProviderList;

        gameProviderList = gameProviderRepo.findByGameType(gameTypeGet);
        System.out.println(gameProviderList);

        List<GameProviderObj> gameProviderObjList = gameProviderList
                .stream()
                .map(
                        obj -> GameProviderObj
                                .builder()
                                .id(obj.getId())
                                .product(obj.getProduct())
                                .productCode(obj.getProductCode())
                                .gameTypeId(obj.getGameType().getId())
                                .imageUrl(obj.getImageName()==null?null:storageService
                                        .getImageByName(obj.getImageName()))
                                .gameTypeName(obj.getGameType().getDescription())
                                .gameTypeCode(obj.getGameType().getCode())
                                .currencyCode(obj.getCurrencyCode())
                                .conversionRate(obj.getConversionRate())
                                .build()
                )
                .collect(Collectors.toList());


        return GameProviderResponse
                .builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Game Provider List By Game Type")
                .gameProviderObjList(gameProviderObjList)
                .build();
    }

    @Override
    public GameProviderResponse deleteProviderById(int providerId) {
        gameProviderRepo.deleteById(providerId);
        return GameProviderResponse
                .builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully deleted provider by ID : "+providerId)
                .build();
    }

}
