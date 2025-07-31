package com.security.spring.ads.rest;

import com.security.spring.ads.dto.AdsResponse;
import com.security.spring.ads.dto.CustomResponse;
import com.security.spring.ads.entity.Ads;
import com.security.spring.ads.service.AdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ads")
public class AdsController {
    private final AdsService adsService;
    @GetMapping
    public ResponseEntity<CustomResponse<AdsResponse>> getListAds(
            @RequestParam(name = "type")Ads.AdsType type
            ){
        return adsService.getListAds(type);
    }
}
