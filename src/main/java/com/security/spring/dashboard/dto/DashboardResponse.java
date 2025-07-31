package com.security.spring.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.spring.spacetechmm.dto.SpaceTechGameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse {
    private int status;
    private String message;
    private List<SpaceTechGameDto> spaceTechGameDtoList;
    private SpaceTechGameDto spaceTechGameDto;

}
