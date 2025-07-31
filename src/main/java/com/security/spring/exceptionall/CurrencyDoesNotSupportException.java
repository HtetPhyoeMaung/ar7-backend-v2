package com.security.spring.exceptionall;

public class CurrencyDoesNotSupportException extends RuntimeException{
    public CurrencyDoesNotSupportException(String message){
        super(message);
    }
}
