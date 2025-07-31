package com.security.spring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "AR7",
                        email = "ar7@gmail.com",
                        url="https://myanmaronlinegameclub.com/"
                ),
                description = "This API is Gambling Game Admin Service With Spring Boot",
                title="AR7 - Game Application",
                version="2.0",
                license = @License(
                        name="Lincense Name",
                        url="teamurl.com"
                ),
                termsOfService = "Terms Of Service"
        ),
        servers ={
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                ),

                @Server(
                        description = "SIT Environment",
                        url = "https://myanmaronlinegameclub.com/"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth Description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
}
