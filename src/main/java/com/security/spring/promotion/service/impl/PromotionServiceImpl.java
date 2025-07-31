package com.security.spring.promotion.service.impl;

import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.promotion.dto.PromotionUnitRequest;
import com.security.spring.promotion.entity.PromotionUnit;
import com.security.spring.promotion.repository.PromotionUnitRepository;
import com.security.spring.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionUnitRepository promotionUnitRepository;

    @Override
    public Map<String, Long> getPromotionUnit() {
        PromotionUnit promotionUnit = promotionUnitRepository.findById(1L).orElseThrow(() -> new DataNotFoundException("promotion not found"));
        return Map.of("PromotionUnit", promotionUnit.getUnit());
    }

    @Override
    public Map<String, Long> setPromotionUnit(PromotionUnitRequest request) {
        if(request.getPromotionUnit() < 0){
            throw new UnauthorizedException("Promotion unit is not less than 0");
        }

        PromotionUnit promotionUnit = promotionUnitRepository.findById(1L).orElseThrow(() -> new DataNotFoundException("promotion not found"));
        promotionUnit.setUnit(request.getPromotionUnit());
        promotionUnit = promotionUnitRepository.save(promotionUnit);
        return Map.of("PromotionUnit",promotionUnit.getUnit());
    }
}
