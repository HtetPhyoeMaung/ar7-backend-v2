package com.security.spring.user.dto;

import com.security.spring.unit.entity.UserUnits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUnitsDto {
    private double mainUnit;
    private double gameUnit;
    private double promotionUnit;
    private int tickets;
    private double turnAmount;

    public static UserUnitsDto of(UserUnits userUnits){
        return UserUnitsDto.builder()
                .mainUnit(userUnits.getMainUnit())
                .gameUnit(userUnits.getGameUnit())
                .promotionUnit(userUnits.getPromotionUnit())
                .tickets(userUnits.getTickets())
                .turnAmount(userUnits.getTurnAmount())
                .build();
    }
}
