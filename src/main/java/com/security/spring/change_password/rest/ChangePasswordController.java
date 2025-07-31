package com.security.spring.change_password.rest;

import com.security.spring.change_password.dto.ChangePasswordRequest;
import com.security.spring.change_password.dto.ChangePasswordResponse;
import com.security.spring.change_password.service.ChangePasswordService;
import com.security.spring.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.naming.NoPermissionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/change-password")
public class ChangePasswordController {
    private final ChangePasswordService changePasswordService;
    private final JWTService jwtService;

    @PostMapping
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestHeader("Authorization") String token,
                                                                 @RequestBody(required = false)ChangePasswordRequest request)
            throws NoPermissionException {
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        ChangePasswordResponse response = changePasswordService.changePassword(request, ar7Id);
        return ResponseEntity.ok(response);
    }
}
