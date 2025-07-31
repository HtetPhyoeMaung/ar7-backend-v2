package com.security.spring.gamesoft.pullPendingRecords.pojo;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullPendingRecordsRequest {
    @NotEmpty
    @NotNull
    @JsonProperty("ProductID")
    private String productID;
    @NotEmpty
    @NotNull
    @JsonProperty("StartDate")
    private String startDate;
    @NotEmpty
    @NotNull
    @JsonProperty("GameType")
    private String gameType;
}
