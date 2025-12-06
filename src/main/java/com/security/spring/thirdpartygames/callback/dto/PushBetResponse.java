package com.security.spring.thirdpartygames.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushBetResponse{

    @JsonProperty("code")
    private int code;
    @JsonProperty("message")
    private String message;

}
