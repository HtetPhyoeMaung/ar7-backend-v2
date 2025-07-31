package com.security.spring.gamesoft.pullReportByWagerIDs.pojo;

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
@AllArgsConstructor
@NoArgsConstructor
public class PullReportByWagerIDsRequest {
    @JsonProperty("OperatorCode")
    private String operatorCode;
    @NotNull
    @NotEmpty
    @JsonProperty("WagerIDs")
    private List<Long> wagerIDs;
}
