package com.security.spring.exceptionall;

public class InvalidWrongSignException extends RuntimeException{
    public InvalidWrongSignException(String message){
        super(message);
    }
}
