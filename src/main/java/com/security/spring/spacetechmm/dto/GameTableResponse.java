package com.security.spring.spacetechmm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameTableResponse {
    private Long id;
    private int miniBet;
    private int maxBet;
    private int level;
    private int bet;
    private Long spaceTechId;
    private String imageURL;
    private LocalDateTime createDate;
    private LocalDateTime upDate;


}