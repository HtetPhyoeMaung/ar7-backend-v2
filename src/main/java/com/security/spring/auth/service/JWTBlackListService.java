package com.security.spring.auth.service;

public interface JWTBlackListService {
    void addToBlackListToken(String token);
    boolean isBlackListToken(String token);
}
