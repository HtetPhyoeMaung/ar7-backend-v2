package com.security.spring.thirdpartygames.callback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaunchGameBankDto {
    private String agentId;
    private String agentPassword;
    private String gameCode;
    private String userName;
    private String displayName;
    private double balance;

    public static LaunchGameBankDto of(String agentId, String agentPassword,String gameCode, String memberName, String displayName, double mainUnit) {
        return LaunchGameBankDto.builder()
                .agentId(agentId)
                .agentPassword(agentPassword)
                .gameCode(gameCode)
                .userName(memberName)
                .displayName(displayName)
                .balance(mainUnit)
                .build();
    }
}
