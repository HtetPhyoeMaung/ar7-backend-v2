package com.security.spring.thirdpartygames.getProviderList.service;

import com.security.spring.thirdpartygames.getProviderList.dto.ProviderResponse;
import org.springframework.http.ResponseEntity;

public interface ProviderListService {
    ResponseEntity<ProviderResponse> getProviderListByGameType(String gameType);

    ResponseEntity<String> syncProviders();
}
