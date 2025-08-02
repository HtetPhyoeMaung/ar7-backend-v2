package com.security.spring.gamesoft.callback.dto;

import java.math.BigDecimal;

public enum Currency {
    MMK(1), MMK2(0.001), IDR(0.001), IDR2(0.001),
    KRW2(0.001), VND2(0.001), LAK2(0.001), KHR2(0.001),
    MMK3(0.01);

    private final double rate;

    Currency(double rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return BigDecimal.valueOf(rate);
    }
}
