package com.security.spring.exceptionall;

public class ApiDuplicateTransaction extends RuntimeException{
    public ApiDuplicateTransaction(String message){
        super(message);
    }
}
