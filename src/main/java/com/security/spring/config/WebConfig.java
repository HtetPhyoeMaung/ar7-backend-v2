package com.security.spring.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://ar7mm.org/",
                        "https://billion123.com/",
                        "http://143.198.209.215",
                        "https://swmd.6633663.com/",
                        "https://myanmaronlinegameclub.com",
                        "https://www.myanmaronlinegameclub.com",
                        "https://ar7mm.org/admin",
                        "https://143.198.209.215",
                        "https://ar7.org/",
                        "http://localhost:4200",
                        "https://ar7myanmar.com/"
                )   
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
