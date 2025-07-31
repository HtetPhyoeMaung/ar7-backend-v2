package com.security.spring.exceptionall;

public class FieldRequireException extends RuntimeException{
    public FieldRequireException(String message){
        super(message);
    }
}
