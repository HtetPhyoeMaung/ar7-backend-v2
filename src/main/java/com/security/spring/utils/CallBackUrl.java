package com.security.spring.utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gameSoft/callback")
public class CallBackUrl {
    @GetMapping("/callbackurl")
    public String getcallback(){
        return "Hello World";
    }
}
