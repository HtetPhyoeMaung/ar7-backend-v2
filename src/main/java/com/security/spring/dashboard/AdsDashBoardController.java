package com.security.spring.dashboard;

import com.security.spring.ads.dto.AdsRequest;
import com.security.spring.ads.dto.AdsResponse;
import com.security.spring.ads.dto.CustomResponse;
import com.security.spring.ads.entity.Ads;
import com.security.spring.ads.service.AdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/dashboard/ads")
@RequiredArgsConstructor
public class AdsDashBoardController {

    private final AdsService adsService;

    @PostMapping
    public ResponseEntity<CustomResponse<AdsResponse>> createAds(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "type") Ads.AdsType type,
            @RequestParam(name = "image", required = false) MultipartFile image
    ) throws IOException {
        AdsRequest adsRequest = AdsRequest.builder()
                .text(text)
                .type(type)
                .image(image)
                .build();
        return adsService.createAds(adsRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<AdsResponse>> updateAds(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "type") Ads.AdsType type,
            @RequestParam(name = "image", required = false) MultipartFile image
    ) throws IOException {
        AdsRequest adsRequest = AdsRequest.builder()
                .id(id)
                .text(text)
                .type(type)
                .image(image)
                .build();
        return adsService.updateAds(adsRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAds(@PathVariable(name = "id") Long id){
        return adsService.deleteAds(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdsResponse> findById(@PathVariable(name = "id") Long id){
        return adsService.searchById(id);
    }
}
