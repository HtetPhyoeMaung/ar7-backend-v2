package com.security.spring.spacetechmm.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpaceTechResponse {
    private String status;
    private String url;
}
