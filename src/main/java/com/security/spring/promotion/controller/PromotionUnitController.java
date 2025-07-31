package com.security.spring.promotion.controller;

import com.security.spring.promotion.dto.PromotionUnitRequest;
import com.security.spring.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotion-unit")
public class PromotionUnitController {
    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<Map<String, Long>> getPromotionUnit(){
        Map<String, Long> promotion = promotionService.getPromotionUnit();
        return ResponseEntity.ok(promotion);
    }

    @PutMapping
    public ResponseEntity<Map<String, Long>> setPromotionUnit(@RequestBody PromotionUnitRequest request){
        Map<String, Long> promotion = promotionService.setPromotionUnit(request);
        return ResponseEntity.ok(promotion);
    }
}
