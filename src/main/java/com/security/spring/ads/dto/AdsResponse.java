package com.security.spring.ads.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.spring.ads.entity.Ads;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdsResponse {

    private Long id;
    private String text;
    private String imageUrl;
    private Ads.AdsType type;

}
