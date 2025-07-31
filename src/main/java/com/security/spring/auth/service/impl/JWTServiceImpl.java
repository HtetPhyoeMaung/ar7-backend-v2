package com.security.spring.auth.service.impl;

import com.security.spring.auth.entity.JWTBlackList;
import com.security.spring.auth.repository.JWTBlackLIstRepository;
import com.security.spring.auth.service.JWTBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTBlackListService {

    private final JWTBlackLIstRepository jwtBlackLIstRepository;

    @Override
    public void addToBlackListToken(String token) {
        jwtBlackLIstRepository.save(JWTBlackList.builder()
                        .token(token)
                        .build());
    }

    @Override
    public boolean isBlackListToken(String token) {
        return jwtBlackLIstRepository.existsByToken(token);
    }
}
