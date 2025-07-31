package com.security.spring.gamesoft.getProviderList.rest;

import com.security.spring.gamesoft.getProviderList.dto.ProviderResponse;
import com.security.spring.gamesoft.getProviderList.service.ProviderListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
