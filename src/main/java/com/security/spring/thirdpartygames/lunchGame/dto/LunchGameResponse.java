package com.security.spring.thirdpartygames.lunchGame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.spring.thirdpartygames.callback.dto.GameBankResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<LunchGameResponse> fromGameBank(GameBankResponse body) {
        return ResponseEntity.ok(LunchGameResponse.builder()
                .errorCode(0)
                        .errorMessage(null)
                        .url(body.getData())
                        .content(null)
                .build());
    }
}
