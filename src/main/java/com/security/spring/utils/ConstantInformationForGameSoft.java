package com.security.spring.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConstantInformationForGameSoft {
    @Builder.Default
//    private String apiUrl = "https://prodmd.9977997.com"; production
    private String apiUrl = "https://staging.gsimw.com/"; // staging
    @Builder.Default
    private String operatorCode = "N5I1";
    @Builder.Default
    private int languageCode = 1;
    @Builder.Default
    private String secretKey ="uahf7D3odaCUGJVddUvHhH";
}
