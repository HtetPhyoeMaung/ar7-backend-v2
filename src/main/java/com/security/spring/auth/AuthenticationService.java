package com.security.spring.auth;

import com.security.spring.auth.service.JWTBlackListService;
import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.*;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.entity.RegisterStatus;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.ComponentRepository;
import com.security.spring.user.repository.RegisterStatusRepo;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.role.Component;
import com.security.spring.user.role.Role;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.AR7IdGenerate;
import com.security.spring.utils.OneTimePasswordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RegisterStatusRepo registerStatusRepo;
    private final ComponentRepository componentRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JWTBlackListService jwtBlackListService;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME=(long) 1000 * 60 * 60 * 24 * 7;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME=(long) 1000 * 60 * 60 * 24 * 7;

    public AuthenticationResponse register(HttpServletRequest request, RegisterRequest registerRequest) {

        UserUnits unit = UserUnits.builder()
                .mainUnit(0)
                .gameUnit(0)
                .promotionUnit(0)
                .tickets(0)
                .build();

        String fullName = "";
        String password = OneTimePasswordService.generateOTP(10);
        String secretCode = OneTimePasswordService.generateSecretKey(6);
        User user;
        String parentAr7Id;
        if(request.getHeader("Authorization") != null ){
            if(registerRequest == null){
                throw new FieldRequireException("register request was required.");
            }
            parentAr7Id = registerRequest.getParentUserId();
            Role downLineRole = registerRequest.getRole();
            String downLineAr7Id = generateAr7Prefix(downLineRole) + AR7IdGenerate.generateDigit();
            password = registerRequest.getPassword();
            secretCode = registerRequest.getSecretCode();
            fullName = registerRequest.getFullName();

            user = User.builder()
                    .userId(0)
                    .name(fullName)
                    .password(passwordEncoder.encode(password))
                    .role(downLineRole)
                    .ar7Id(downLineAr7Id)
                    .status(true)
                    .parentUserId(parentAr7Id)
                    .userUnits(unit)
                    .secretCode(secretCode)
                    .build();
            user = userRepository.save(user);

        }else {
            parentAr7Id = "AG0000009";
            String ar7Id = generateAr7Prefix(Role.USER) + AR7IdGenerate.generateDigit();
            if(registerRequest == null){

                user = User.builder()
                        .userId(0)
                        .name(ar7Id)
                        .password(passwordEncoder.encode(password))
                        .role(Role.USER)
                        .ar7Id(ar7Id)
                        .status(true)
                        .parentUserId(parentAr7Id)
                        .userUnits(unit)
                        .secretCode(secretCode)
                        .build();
                user = userRepository.save(user);
            }else{
                user = User.builder()
                        .userId(0)
                        .name(registerRequest.getFullName())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .role(registerRequest.getRole())
                        .ar7Id(ar7Id)
                        .status(true)
                        .parentUserId(parentAr7Id)
                        .userUnits(unit)
                        .secretCode(registerRequest.getSecretCode())
                        .build();
                password = registerRequest.getPassword();
                user = userRepository.save(user);
            }
        }

        RegisterStatus registerStatusObj = RegisterStatus.builder()
                .user(user)
                .registerStatus(1)
                .build();

        registerStatusRepo.save(registerStatusObj);

        // Generate tokens using updated JWTService methods
        var accessToken = jwtService.generateAccessToken(user); // 15-minute access token
        var refreshToken = jwtService.generateRefreshToken(user);
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);// 7-day refresh token stored in Redis
        user = userRepository.save(user);

        log.info("Generated access token: {}", accessToken);
        log.info("Generated refresh token: {}", refreshToken);


        return AuthenticationResponse.builder()
                .token(accessToken)
                .name(user.getName())
                .parentAr7Id(user.getParentUserId())
                .password(password)
                .role(user.getRole())
                .secretCode(user.getSecretCode())
                .ar7Id(user.getAr7Id())
                .status(true)
                .build();
    }


    private String generateAr7Prefix(Role downLineRole) {
        String ar7IdPrefix = "";
        if ("USER".equalsIgnoreCase(downLineRole.toString())) {
            ar7IdPrefix = "";
        } else if ("AFFILIATEAGENT".equalsIgnoreCase(downLineRole.toString())) {
            ar7IdPrefix = "AFG";
        } else if ("AGENT".equalsIgnoreCase(downLineRole.toString())) {
            ar7IdPrefix = "AG";
        } else if ("MASTER".equalsIgnoreCase(downLineRole.toString())) {
            ar7IdPrefix = "MS";
        } else if ("SENIORMASTER".equalsIgnoreCase(downLineRole.toString())) {
            ar7IdPrefix = "SE";
        } else if ("ADMIN".equalsIgnoreCase(downLineRole.toString())) {
            ar7IdPrefix = "AY";
        }
        return ar7IdPrefix;
    }

    private Role checkRole(String parentRole) {
        if(parentRole.equals(Role.ADMIN.name())){
            return Role.SENIORMASTER;
        }else if(parentRole.equals(Role.SENIORMASTER.name())){
            return Role.MASTER;
        }else if(parentRole.equals(Role.MASTER.name())){
            return Role.AGENT;
        }else {
            return Role.USER;
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (request.getAr7Id() == null || request.getPassword() == null ||
                request.getAr7Id().isEmpty() || request.getPassword().isEmpty()) {
            throw new FieldRequireException("Field were require to login");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getAr7Id(),
                            request.getPassword()
                    )
            );
        } catch (RuntimeException e) {
            throw new InvalidException("ar7Id and password is not valid.");
        }

        User user = repository.findByAr7Id(request.getAr7Id())
                .orElseThrow(() -> new DataNotFoundException("user can't found."));

        user.setLoginTime(LocalDateTime.now());

        if(user.getRefreshToken() != null){
            jwtBlackListService.addToBlackListToken(user.getRefreshToken());
        }

        if(user.getAccessToken() != null){
            jwtBlackListService.addToBlackListToken(user.getAccessToken());
        }

        // Generate tokens using updated JWTService methods
        var accessToken = jwtService.generateAccessToken(user); // 15-minute access token
        var refreshToken = jwtService.generateRefreshToken(user);// 7-day refresh token stored in Redis
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        user = repository.save(user);// Still save for loginTime update
        Component component = componentRepository.findByRole(user.getRole());


        return AuthenticationResponse.builder()
                .token(accessToken)
                .name(user.getName())
                .parentAr7Id(user.getParentUserId())
                .secretCode(user.getSecretCode())
                .role(user.getRole())
                .component(component.getPermissionCode())
                .ar7Id(user.getAr7Id())
                .status(true)
                .build();
    }


    public TokenResponse getRefreshToken(String ar7Id) {
        User user = userService.findByAr7Id(ar7Id);
        String refreshToken = user.getRefreshToken();
        if(refreshToken == null){
            throw new TokenExpiredException("please login first");
        }
        return TokenResponse.builder().refreshToken(AR7IdGenerate.generateKey(5)+refreshToken).build();
    }

    public TokenResponse generateAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User user = userService.findByAr7Id(username);
        if(user.getAccessToken() != null){
            jwtBlackListService.addToBlackListToken(user.getAccessToken());
        }
        String accessToken = jwtService.generateAccessToken(user);
        user.setAccessToken(accessToken);
        userRepository.save(user);
        return TokenResponse.builder().accessToken(accessToken).build();
    }
}
