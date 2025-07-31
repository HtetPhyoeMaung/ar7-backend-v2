package com.security.spring.promotion.service;

import com.security.spring.promotion.dto.PromotionUnitRequest;

import java.util.Map;

public interface PromotionService {
    Map<String, Long> getPromotionUnit();

    Map<String, Long> setPromotionUnit(PromotionUnitRequest request);
}
