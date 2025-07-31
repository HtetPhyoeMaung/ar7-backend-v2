package com.security.spring.gamesoft.pullReport.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PullReportRequest {
    @JsonProperty("OperatorCode")
    private String operatorCode;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("Sign")
    private String sign;
    @JsonProperty("RequestTime")
    private String requestTime;

}
