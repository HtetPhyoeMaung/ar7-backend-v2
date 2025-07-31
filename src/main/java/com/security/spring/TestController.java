package com.security.spring;

import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/server")
    public ResponseEntity<String> testServer(){
        return ResponseEntity.ok("Server's working...");
    }
    @GetMapping("/serverTime")
    public ResponseEntity<String> testServerTime(){
        return ResponseEntity.ok("Server UTC Time : "+ LocalDateTime.now());
    }
}
