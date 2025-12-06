package com.security.spring.thirdpartygames.lunchGame.service;

import com.security.spring.thirdpartygames.lunchGame.dto.LunchGameRequest;
import com.security.spring.thirdpartygames.lunchGame.dto.LunchGameResponse;

public interface LunchGameService {
    public LunchGameResponse lunchGameService(LunchGameRequest data,String ar7Id);
}
