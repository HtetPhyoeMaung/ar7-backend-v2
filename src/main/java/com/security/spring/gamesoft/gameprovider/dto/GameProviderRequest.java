package com.security.spring.gamesoft.gameprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameProviderRequest {
    private Integer id;
    private Long product;
    private String productCode;
    private Integer gameTypeCode;
    private String currencyCode;
    private MultipartFile image;
    private Double conversionRate;
}
