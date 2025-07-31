package com.security.spring.exceptionall;

public class CustomAlreadyExistException extends RuntimeException{
    public CustomAlreadyExistException(String message){
        super(message);
    }
}
