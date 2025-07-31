package com.security.spring.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameRequest {
    private String gameCode;

    private String gameName;

    private int gameType;

    private MultipartFile image;
}
