package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.thirdpartygames.callback.dto.PushBetRequest;
import com.security.spring.thirdpartygames.callback.dto.PushBetResponse;

public interface PushBetService {
     PushBetResponse pushbetConfig(PushBetRequest data);
}
