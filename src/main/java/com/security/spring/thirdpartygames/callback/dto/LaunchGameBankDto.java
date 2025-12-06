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

    public static LaunchGameBankDto of(String memberName, String displayName, double mainUnit) {
        return LaunchGameBankDto.builder()
                .agentId("QJFDkw7P")
                .agentPassword("51ac6d7e-c857-48c5-bf30-fcf837d40223")
                .gameCode("b001")
                .userName(memberName)
                .displayName(displayName)
                .balance(mainUnit)
                .build();
    }
}
