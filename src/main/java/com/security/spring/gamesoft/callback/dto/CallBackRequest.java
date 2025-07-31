package com.security.spring.gamesoft.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallBackRequest {
    @JsonProperty("batch_requests")
    private List<BatchRequest> batchRequests;
    @JsonProperty("operator_code")
    private String operatorCode;
    @JsonProperty("currency")
    private String  currency;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("request_time")
    private int requestTime;

}
