package com.security.spring.gamesoft.markReport.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkReportRequest {
    @NotNull
    @NotEmpty
    @JsonProperty("WagerID")
    private List<Long> wagerIDs;
}
