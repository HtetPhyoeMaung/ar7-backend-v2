package com.security.spring.gamesoft.callback.dto;

import java.math.BigDecimal;

public enum Currency {
    MMK(1), MMK2(1000), MMK3(100);

    private final double rate;

    Currency(double rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return BigDecimal.valueOf(rate);
    }
}
