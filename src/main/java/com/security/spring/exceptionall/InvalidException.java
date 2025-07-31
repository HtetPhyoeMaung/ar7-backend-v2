package com.security.spring.exceptionall;

public class InvalidException extends RuntimeException{
    public InvalidException(String message){
        super(message);
    }
}
