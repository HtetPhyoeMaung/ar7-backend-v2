package com.security.spring.thirdpartygames.callback.dto;

import lombok.Data;

import java.time.OffsetDateTime;
@Data
public class GameBankResponse {
    private String title;
    private String status;
    private int statusCode;
    private String message;
    private OffsetDateTime timestamp;

    private String  data;
}
