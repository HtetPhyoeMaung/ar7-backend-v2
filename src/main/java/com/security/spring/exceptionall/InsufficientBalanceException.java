package com.security.spring.exceptionall;

import lombok.Getter;

@Getter
public class InsufficientBalanceException extends RuntimeException{
    private String memberAccount;
    public InsufficientBalanceException(String message, String memberAccount) {
        super(message);
        this.memberAccount = memberAccount;
    }
}
