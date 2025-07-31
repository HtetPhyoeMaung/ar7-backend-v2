package com.security.spring.ads.service;

import com.security.spring.ads.dto.AdsRequest;
import com.security.spring.ads.dto.AdsResponse;
import com.security.spring.ads.dto.CustomResponse;
import com.security.spring.ads.entity.Ads;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AdsService {
    ResponseEntity<CustomResponse<AdsResponse>> createAds(AdsRequest adsRequest) throws IOException;

    ResponseEntity<CustomResponse<AdsResponse>> updateAds(AdsRequest adsRequest) throws IOException;

    ResponseEntity<String> deleteAds(Long id);

    ResponseEntity<AdsResponse> searchById(Long id);

    ResponseEntity<CustomResponse<AdsResponse>> getListAds(Ads.AdsType type);
}
