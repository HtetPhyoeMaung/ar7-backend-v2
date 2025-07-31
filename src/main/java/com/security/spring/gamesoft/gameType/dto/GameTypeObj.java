package com.security.spring.gamesoft.gameType.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameTypeObj {
    private Integer id;
    private String code;
    private String description;
}
