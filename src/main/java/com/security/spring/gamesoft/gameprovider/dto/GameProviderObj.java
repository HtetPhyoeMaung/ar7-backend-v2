package com.security.spring.gamesoft.gameprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameProviderObj {
    private Integer id;
    private Long product;
    private String productCode;
    private Integer gameTypeId;
    private String gameTypeName;
    private String currencyCode;
    private Double conversionRate;
    private String imageUrl;
    private String gameTypeCode;
}
