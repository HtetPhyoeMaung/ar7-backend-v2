package com.security.spring.gamesoft.markReport.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkReportResponse {
    @JsonProperty("ErrorCode")
    private String ErrorCode;
    @JsonProperty("ErrorMessage")
    private String ErrorMessage;
}
