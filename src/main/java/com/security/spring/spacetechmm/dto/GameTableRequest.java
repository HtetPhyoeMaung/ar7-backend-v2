package com.security.spring.spacetechmm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameTableRequest {
    @NotNull
    private Integer miniBet;
    @NotNull
    private Integer maxBet;
    @NotNull
    private Integer bet;
    @NotNull
    private Integer level;
    private Long spaceTechId;
    private MultipartFile image;

}