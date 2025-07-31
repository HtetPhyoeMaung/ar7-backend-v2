package com.security.spring.gamesoft.gameprovider.rest;

import com.security.spring.gamesoft.gameprovider.dto.GameProviderResponse;
import com.security.spring.gamesoft.gameprovider.service.GameSoftGameProviderService;
import com.security.spring.rro.GameSoftProviderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/gameproduct")
@RequiredArgsConstructor
public class GameSoftGameProductController {


    private final GameSoftGameProviderService gameSoftGameProviderService;

    @PostMapping("/gameprovider")
    public ResponseEntity<GameProviderResponse> createGameProvider(@RequestParam(name = "product") Long product,
                                                @RequestParam(name = "productCode") String productCode,
                                                @RequestParam(name = "gameTypeCode") String  gameTypeCode,
                                                @RequestParam(name = "currencyCode") String currencyCode,
                                                @RequestParam(name = "conversionRate") Double conversionRate,
                                                @RequestParam(name = "image")MultipartFile image) throws IOException {
        GameSoftProviderRequest data = GameSoftProviderRequest.builder()
                .product(product)
                .productCode(productCode)
                .gameTypeCode(gameTypeCode)
                .currencyCode(currencyCode)
                .conversionRate(conversionRate)
                .image(image)
                .build();
        GameProviderResponse resObj = gameSoftGameProviderService.saveGameProvider(data);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/gameprovider")
    public ResponseEntity<GameProviderResponse> getAllProvider(){
        GameProviderResponse resObj = gameSoftGameProviderService.getAllGameProvider();
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/gameproviderByGameType")
    public ResponseEntity<GameProviderResponse> getProviderByGameType(@RequestParam Integer gameTypeId){
        GameProviderResponse resObj = gameSoftGameProviderService.getGameProviderByGameType(gameTypeId);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/gameprovider/{providerId}")
    public ResponseEntity<GameProviderResponse> getProviderById(@PathVariable int providerId){
        GameProviderResponse responseObj = gameSoftGameProviderService.providerFindById(providerId);
        return ResponseEntity.ok().body(responseObj);
    }
    @DeleteMapping("/gameprovider/{providerId}")
    public ResponseEntity<GameProviderResponse> deleteProviderById(@PathVariable(name = "providerId") int providerId){
        GameProviderResponse response = gameSoftGameProviderService.deleteProviderById(providerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/gameprovider/{id}")
    public ResponseEntity<GameProviderResponse> updateGameProvider(@PathVariable("id") int id,
                                                                   @RequestParam(name = "product",required = false) Long product,
                                                @RequestParam(name = "productCode",required = false) String productCode,
                                                @RequestParam(name = "gameTypeCode",required = false) String  gameTypeCode,
                                                @RequestParam(name = "currencyCode",required = false) String currencyCode,
                                                @RequestParam(name = "conversionRate",required = false) Double conversionRate,
                                                @RequestParam(name = "image",required = false)MultipartFile image) throws IOException {
        GameSoftProviderRequest data = GameSoftProviderRequest.builder()
                .product(product)
                .productCode(productCode)
                .gameTypeCode(gameTypeCode)
                .currencyCode(currencyCode)
                .conversionRate(conversionRate)
                .image(image)
                .build();
        GameProviderResponse resObj = gameSoftGameProviderService.updateGameProvider(id,data);
        return ResponseEntity.ok().body(resObj);
    }
}
