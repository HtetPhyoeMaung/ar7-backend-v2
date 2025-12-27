package com.security.spring.config;

import com.security.spring.utils.ConstantInformationForGameSoft;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameSoftConfig {

    @Bean
    public ConstantInformationForGameSoft constantInformationForGameSoft() {
        return ConstantInformationForGameSoft.builder().build();
    }
}
