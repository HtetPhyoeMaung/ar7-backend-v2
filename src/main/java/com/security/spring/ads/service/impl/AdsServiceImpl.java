package com.security.spring.ads.service.impl;

import com.security.spring.ads.dto.AdsRequest;
import com.security.spring.ads.dto.AdsResponse;
import com.security.spring.ads.dto.CustomResponse;
import com.security.spring.ads.entity.Ads;
import com.security.spring.ads.repo.AdsRepository;
import com.security.spring.ads.service.AdsService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.storage.StorageService;
import com.security.spring.utils.Constraint;
import com.security.spring.utils.MapperUtil;
import com.security.spring.utils.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<CustomResponse<AdsResponse>> createAds(AdsRequest adsRequest) throws IOException {
        Ads ads = new Ads();
        switch (adsRequest.getType()) {
            case Carousal:
                ads.setImageName(
                        storageService.uploadImage(adsRequest.getImage())
                );
            case RunningText:
                ads.setText(adsRequest.getText());
        }
        ads.setAdsType(adsRequest.getType());
        ads = adsRepository.save(ads);

        CustomResponse<AdsResponse> response = new CustomResponse<>();
        response.setStatus(true);
        response.setMessage(Constraint.CREATED_SUCCESS_MESSAGE);
        response.setData(List.of(objectMapper.mapToAdsResponse(ads)));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CustomResponse<AdsResponse>> updateAds(AdsRequest adsRequest) throws IOException {
        Ads existAds = findById(adsRequest.getId());
        switch (adsRequest.getType()) {
            case Carousal:
                existAds.setImageName(
                        storageService.updateImage(adsRequest.getImage(),existAds.getImageName())
                );
            case RunningText:
                existAds.setText(adsRequest.getText());
        }
        existAds.setAdsType(adsRequest.getType());
        existAds = adsRepository.save(existAds);
        CustomResponse<AdsResponse> response = new CustomResponse<>();
        response.setStatus(true);
        response.setMessage(Constraint.UPDATED_SUCCESS_MESSAGE);
        response.setData(List.of(objectMapper.mapToAdsResponse(existAds)));
        return ResponseEntity.ok(response);
    }

    public Ads findById(Long id){
        return adsRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Ads not found by Id : "+id));
    }

    @Override
    public ResponseEntity<String> deleteAds(Long id) {
        Ads existAds = findById(id);
        adsRepository.delete(existAds);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AdsResponse> searchById(Long id) {
        return ResponseEntity.ok(objectMapper.mapToAdsResponse(findById(id)));
    }

    @Override
    public ResponseEntity<CustomResponse<AdsResponse>> getListAds(Ads.AdsType type) {
        List<Ads> adsList = adsRepository.findByAdsType(type,Sort.by(Sort.Direction.DESC,"id"));
        CustomResponse<AdsResponse> response = new CustomResponse<>();
        response.setStatus(true);
        response.setMessage(Constraint.RETRIEVE_SUCCESS_MESSAGE);
        response.setData(adsList.stream().map(objectMapper::mapToAdsResponse).toList());
        return ResponseEntity.ok(response);
    }


}
