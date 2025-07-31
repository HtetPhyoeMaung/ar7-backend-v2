package com.security.spring.gamesoft.lunchGame.service;

import com.security.spring.gamesoft.lunchGame.dto.LunchGameRequest;
import com.security.spring.gamesoft.lunchGame.dto.LunchGameResponse;

public interface LunchGameService {
    public LunchGameResponse lunchGameService(LunchGameRequest data,String ar7Id);
}
