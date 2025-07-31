package com.security.spring.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransitionObj {
    private int id;
    private String backendTransitionId;
    private String fromUserAr7Id;
    private String toUserAr7Id;
    private String fromUserName;
    private String toUserName;
    private Double amount;
    private Double fromUserBeforeBalance;
    private Double fromUserAfterBalance;
    private Double toUserBeforeBalance;
    private Double toUserAfterBalance;
    private LocalDateTime transitionTime;
    private String description;
    private String transitionStatus;
}
