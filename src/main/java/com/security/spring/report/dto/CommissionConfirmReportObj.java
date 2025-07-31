package com.security.spring.report.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommissionConfirmReportObj {
    private String agentAr7Id;
    private Double agentWinLoseAmount;
    private String masterAr7Id;
    private Double masterWinLoseAmount;
    private String seMasterA7Id;
    private Double seMasterWinLoseAmount;
    private LocalDateTime confirmDate;
}
