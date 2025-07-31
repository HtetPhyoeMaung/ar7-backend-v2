package com.security.spring.gamesoft.fetchReport.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchReportRequest {
    @JsonProperty("OperatorCode")
    private String operatorCode;
}
