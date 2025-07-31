package com.security.spring.gamesoft.callback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payload {
    private double amount;
    private String providerTxId;
    private String providerUsername;
    private String roundDetails;
    private String username;
}

