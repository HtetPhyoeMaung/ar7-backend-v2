package com.security.spring.gamesoft.getProviderList.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

    @Data
    @JsonNaming(com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
    public  class ProviderDataFeign {
        private String provider;
        private String currency;
        private String status;
        private Long providerId;
        private Long productId;
        private Long productCode;
        private String gameType;
        private String productName;
    }

