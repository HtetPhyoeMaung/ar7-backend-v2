package com.security.spring.exceptionall;

public class TokenHasBeenBlackListedException extends RuntimeException{
    public TokenHasBeenBlackListedException(String message){
        super(message);
    }
}
