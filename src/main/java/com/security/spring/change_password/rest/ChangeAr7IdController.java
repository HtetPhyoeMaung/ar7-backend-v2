package com.security.spring.change_password.rest;

import com.security.spring.change_password.dto.ChangeAr7IdResponse;
import com.security.spring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/change-id")
public class ChangeAr7IdController {
    private final UserService userService;
    @PostMapping("/{newAr7Id}")
    public ResponseEntity<ChangeAr7IdResponse> changeAr7IdByOwner(@PathVariable String newAr7Id){
        ChangeAr7IdResponse changeAr7IdResponse = userService.changeAr7IdByOwner(newAr7Id);
        return ResponseEntity.ok(changeAr7IdResponse);
    }
}
