package com.security.spring.commission.dto;

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
public class CommissionRequest {
    private Integer id;
    private float commission;

    @NotNull( message = "GameTypeCode Must Not Null")
    private String  gameTypeCode;

    @NotEmpty(message = "Please  Enter DownLineAr7Id")
    @NotNull( message = "DownLineAr7Id Must Not Null")
    private String downLineAr7Id;

}
