package com.security.spring.ads.dto;

import com.security.spring.ads.entity.Ads;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdsRequest {
    private Long id;
    private String text;
    private MultipartFile image;
    private Ads.AdsType type;
}
