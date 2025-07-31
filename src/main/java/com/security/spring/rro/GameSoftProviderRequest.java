package com.security.spring.rro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSoftProviderRequest {
    private Integer id;
    private Long product;
    private String productCode;
    private String  gameTypeCode;
    private String currencyCode;
    private double conversionRate;
    private MultipartFile image;
}
