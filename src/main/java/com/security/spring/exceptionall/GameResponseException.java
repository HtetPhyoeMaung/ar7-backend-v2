package com.security.spring.exceptionall;

public class GameResponseException extends RuntimeException{

    public GameResponseException(String message){
        super(message);
    }
}
