package com.security.spring.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/access-token")
public class AccessTokenController {
    private final AuthenticationService service;

    @GetMapping
    public ResponseEntity<TokenResponse> getAccessToken(@RequestHeader("Authorization") String headerToken){
        String refreshToken = headerToken.substring(7);
        TokenResponse tokenResponse = service.generateAccessToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }
}
