package com.security.spring.spacetechmm.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpaceTechGameDto {

    private Long id;

    private String gameCode;

    private String gameName;

    private Long gameTypeId;

    private String category;

    private String  gameTypeName;

    private String imageUrl;
}
