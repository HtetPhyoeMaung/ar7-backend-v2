package com.security.spring.gamesoft.getProviderList.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderResponse {

    private Integer code;
    private String message;
    private List<ProviderData> providers;



    @Data
    @Builder
    public static class ProviderData {
        private String provider;
        private String currency;
        private String status;
        private Long providerId;
        private Long productId;
        private Long productCode;
        private String gameType;
        private String productName;

        public static ProviderData of(ProviderDataFeign providerDataFeign) {
            return ProviderData.builder()
                    .provider(providerDataFeign.getProvider())
                    .currency(providerDataFeign.getCurrency())
                    .status(providerDataFeign.getStatus())
                    .providerId(providerDataFeign.getProviderId())
                    .productId(providerDataFeign.getProductId())
                    .productCode(providerDataFeign.getProductCode())
                    .gameType(providerDataFeign.getGameType())
                    .productName(providerDataFeign.getProductName())
                    .build();
        }
    }

}
