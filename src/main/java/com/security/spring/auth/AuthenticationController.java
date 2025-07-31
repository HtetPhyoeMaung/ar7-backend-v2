package com.security.spring.auth;

import com.security.spring.auth.service.JWTBlackListService;
import com.security.spring.config.JWTService;
import com.security.spring.user.entity.User;
import com.security.spring.user.role.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final JWTBlackListService jwtBlackListService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(HttpServletRequest httpServletRequest,
                                                           @RequestBody(required = false) RegisterRequest request){
        return ResponseEntity.ok(service.register(httpServletRequest ,request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            String username = jwtService.validateAndGetUsernameFromRefreshToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String newAccessToken = jwtService.generateAccessToken(userDetails);
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("refreshToken", refreshToken); // Reuse existing refresh token
            logger.info("Access token refreshed for user: {}", username);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid refresh token: {}", refreshToken);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader(value = "Refresh-Token", required = false) String refreshToken) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Invalid Authorization header received");
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        try {
            jwtBlackListService.addToBlackListToken(token);
            if (refreshToken != null) {
                jwtBlackListService.addToBlackListToken(refreshToken);
            }
            logger.info("User logged out successfully");
            return ResponseEntity.ok("Successfully logged out");
        } catch (IllegalStateException e) {
            logger.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.status(503).body("Unable to logout: Redis service unavailable");
        }
    }

    @GetMapping("/role")
    public ResponseEntity<List<String>> getRoleAll(){
        List<String> role = Arrays.stream(Role.values()).map(Enum::name)
                .toList();
        return ResponseEntity.ok(role);
    }

    @GetMapping("/{ar7Id}/refresh-token")
    public ResponseEntity<TokenResponse> getRefreshToken(@PathVariable("ar7Id") String ar7Id){
        TokenResponse token = service.getRefreshToken(ar7Id);
        return ResponseEntity.ok(token);
    }

}
