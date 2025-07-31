package com.security.spring.gamesoft.markReport.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkReportRequestToProvider {

    @JsonProperty("OperatorCode")
    private String operatorCode;

    @JsonProperty("WagerID")
    private List<Long> wagerIDs;
}
