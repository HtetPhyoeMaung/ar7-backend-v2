package com.security.spring.demo;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/democontroller")
public class DemoController {
    @GetMapping
    public ResponseEntity<String> simpleRequest(){
        return ResponseEntity.ok("Hello This is Simple Request");
    }
}
