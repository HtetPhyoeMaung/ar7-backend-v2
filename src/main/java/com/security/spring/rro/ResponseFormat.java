package com.security.spring.rro;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFormat {
    private String message;
    private boolean status;
    private Integer statusCode;
}
