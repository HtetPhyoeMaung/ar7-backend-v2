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
    private String apiUrl = "https://production.gsimw.com/";
    @Builder.Default
    private String operatorCode = "N5I1";
    @Builder.Default
    private int languageCode = 0;
    @Builder.Default
    private String secretKey ="fooaVY5hy2Za6jpvdC9KHc";
}
