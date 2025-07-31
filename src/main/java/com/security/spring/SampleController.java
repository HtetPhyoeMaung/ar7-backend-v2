package com.security.spring;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Sample API", description = "This is a sample API.")
public class SampleController {


    @GetMapping("/hello")
    @Operation(summary = "Say Hello", description = "Returns a greeting message.")
    public String sayHello() {
        return "Hello, Swagger!";
    }
}
