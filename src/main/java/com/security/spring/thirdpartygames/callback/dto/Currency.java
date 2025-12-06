package com.security.spring.thirdpartygames.callback.dto;

import java.math.BigDecimal;

public enum Currency {
    MMK(1), MMK2(1000), IDR(1), IDR2(1000),
    KRW2(1000), VND2(1000), LAK2(1000), KHR2(1000),
    MMK3(100);

    private final double rate;

    Currency(double rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return BigDecimal.valueOf(rate);
    }
}
