package com.security.spring.exceptionall;

public class BetNotExistsException extends RuntimeException{
    public BetNotExistsException(String message){
        super(message);
    }
}
