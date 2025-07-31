package com.security.spring.commission.dto;

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
public class CommissionConfirmObj {
    private String agentAr7Id;
    private double beforeAgentBalance;
    private double afterAgentBalance;
    private double agentWinLoseAmount;
    private String masterAr7Id;
    private double beforeMasterBalance;
    private double afterMasterBalance;
    private double masterWinLoseAmount;
    private String seMasterA7Id;
    private double beforeSeMasterBalance;
    private double afterSeMasterBalance;
    private double seMasterWinLoseAmount;
    private LocalDateTime confirmDate;
}
