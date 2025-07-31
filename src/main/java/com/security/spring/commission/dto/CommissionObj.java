package com.security.spring.commission.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.spring.gamesoft.gameType.dto.GameTypeObj;
import com.security.spring.user.dto.UserResponseObj;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommissionObj {
    private int id;
    private String ar7Id;
    private boolean status;
    private double betAmount;
    private double winAmount;
    private GameTypeObj gameTypeObj;
    private UserResponseObj downLineUser;
    private double winLoseAmount;
    private double upLineWinLose;
    private String seAr7Id;
    private double seCommission;
    private double seCommissionPercentage;
    private String msAr7Id;
    private double msCommission;
    private double msCommissionPercentage;
    private String agAr7Id;
    private double agCommission;
    private double agCommissionPercentage;
    private String gameTypeName;
    private String  gameTypeCode;
    private double upLineRtp;

}
