package com.security.spring.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JWTService {
    private static final String SECRET_KEY = "MHcCAQEEIEDU2eznQf8BTejgYj8TXtNa5qLVo5zk8thANy7Kd9hdoAoGCCqGSM49AwEHoUQDQgAECAuTIWGTIvwAdHfvOEjHCqg3oFT37ciY6RVBJYOK2xyqPGzREyo16sQK4zCofVBaXBE+1FjNoGFjcp7j3nnqtg==";
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Access Token Methods
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, long tokenExpirationTime) {
        return generateToken(new HashMap<>(), userDetails, tokenExpirationTime);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long tokenExpirationTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token Methods
    public String generateRefreshToken(UserDetails userDetails) {
//        String refreshToken = UUID.randomUUID().toString();
//        String redisKey = "refresh:" + refreshToken;
//        redisTemplate.opsForValue().set(redisKey, userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);
//
//        logger.info("Generated refresh token for user: {}", userDetails.getUsername());
        return generateToken(userDetails, REFRESH_TOKEN_EXPIRATION);
    }


    public String validateAndGetUsernameFromRefreshToken(String refreshToken) {
        String redisKey = "refresh:" + refreshToken;
        String username = redisTemplate.opsForValue().get(redisKey);
        if (username == null) {
            logger.warn("Invalid or expired refresh token: {}", refreshToken);
            throw new IllegalArgumentException("Invalid refresh token");
        }
        return username;
    }

    public void invalidateRefreshToken(String refreshToken) {
        String redisKey = "refresh:" + refreshToken;
        redisTemplate.delete(redisKey);
        logger.info("Refresh token invalidated: {}", refreshToken);
    }

    // Blacklist Methods
    public void blacklistToken(String token) {
        try {
            Date expiration = extractExpiration(token);
            long ttl = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            if (ttl > 0) {
                redisTemplate.opsForValue().set(token, "blacklisted", ttl, TimeUnit.SECONDS);
                logger.info("Token blacklisted successfully: {}", token);
            }
        } catch (Exception e) {
            logger.error("Failed to blacklist token: {}", e.getMessage());
            throw new IllegalStateException("Unable to connect to Redis", e);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        try {
            return redisTemplate.opsForValue().get(token) != null;
        } catch (Exception e) {
            logger.error("Error checking token blacklist status: {}", e.getMessage());
            return false;
        }
    }

    // Validation Methods
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // New convenience methods for access token
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, ACCESS_TOKEN_EXPIRATION);
    }
}