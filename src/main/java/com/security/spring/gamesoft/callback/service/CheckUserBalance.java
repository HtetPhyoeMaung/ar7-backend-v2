package com.security.spring.gamesoft.callback.service;

import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckUserBalance {

    public boolean validateBalance(User user) {
        UserUnits userUnits = user.getUserUnits();
        return userUnits == null || userUnits.getMainUnit() <= 0;
    }
}

