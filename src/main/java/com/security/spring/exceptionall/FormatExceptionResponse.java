package com.security.spring.exceptionall;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormatExceptionResponse {
    @JsonProperty("code")
    private int errorCode;
    @JsonProperty("message")
    private String errorMessage;
    private Boolean status;
}
