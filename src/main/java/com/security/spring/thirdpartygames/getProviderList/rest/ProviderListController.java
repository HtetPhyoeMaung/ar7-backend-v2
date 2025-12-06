package com.security.spring.thirdpartygames.getProviderList.rest;

import com.security.spring.thirdpartygames.getProviderList.dto.ProviderResponse;
import com.security.spring.thirdpartygames.getProviderList.service.ProviderListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/provider-list")
@RequiredArgsConstructor
public class ProviderListController {

    private final ProviderListService providerListService;

    @GetMapping
    public ResponseEntity<ProviderResponse> getProviderListByGameType(
            @RequestParam(value = "gameTypeCode", required = false) String gameType) {
        return providerListService.getProviderListByGameType(gameType);
    }

    @PutMapping("/sync-system")
    public ResponseEntity<String> syncProvider(){
        return providerListService.syncProviders();
    }

}
