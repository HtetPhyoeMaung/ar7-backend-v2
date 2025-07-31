package com.security.spring.utils;

import java.math.BigDecimal;

public class CurrencyUtil {
    private CurrencyUtil(){

    }
    public static BigDecimal getCurrencyRate(String currencyCode) {
        return switch (currencyCode) {
            case "IDR2", "IRR2", "KHR2", "KRW2", "LAK2", "LBP2", "MMK2", "UZS2", "VND2", "COP2", "PYG2" -> new BigDecimal("0.001");
            case "MMK" -> new BigDecimal("1");
            case "MMK3" -> new BigDecimal("0.01");
            default -> BigDecimal.ONE;
        };
    }


}
