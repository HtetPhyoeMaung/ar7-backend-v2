package com.security.spring.rro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransitionHistoryResponse {
    private String tranitionId;
    private double transferAmount;
    private LocalDateTime transferDate;
    private String fromUserName;
    private String toUserName;
    private String remark;

}
