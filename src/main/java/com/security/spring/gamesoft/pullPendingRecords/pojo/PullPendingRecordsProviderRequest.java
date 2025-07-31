package com.security.spring.gamesoft.pullPendingRecords.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullPendingRecordsProviderRequest {
    @JsonProperty("OperatorCode")
    private String operatorCode;
    @JsonProperty("ProductID")
    private String productID;
    @JsonProperty("GameType")
    private String gameType;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
}
