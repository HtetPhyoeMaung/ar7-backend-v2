package com.security.spring.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameTypeCommissionObj {
    private int id;
    private String gameType;
    private String commission;
    private String ar7Id;
    private String parentUserId;
}
