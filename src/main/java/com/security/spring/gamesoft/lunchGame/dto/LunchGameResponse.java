package com.security.spring.gamesoft.lunchGame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LunchGameResponse {
    @JsonProperty("code")
    private Integer errorCode;
    @JsonProperty("message")
    private String errorMessage;
    @JsonProperty("url")
    private String url;
    @JsonProperty("content")
    private String content;
}
