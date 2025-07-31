package com.security.spring.exceptionall;

public class ApiMemberDoesNotExist extends RuntimeException{
    public ApiMemberDoesNotExist(String message){
        super(message);
    }
}
