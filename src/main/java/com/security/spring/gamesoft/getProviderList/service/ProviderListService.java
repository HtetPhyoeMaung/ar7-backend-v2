package com.security.spring.gamesoft.getProviderList.service;

import com.security.spring.gamesoft.getProviderList.dto.ProviderResponse;
import org.springframework.http.ResponseEntity;

public interface ProviderListService {
    ResponseEntity<ProviderResponse> getProviderListByGameType(String gameType);
}
