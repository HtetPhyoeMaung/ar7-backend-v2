package com.security.spring.gamesoft.callback.service;

import com.security.spring.gamesoft.callback.dto.PushBetRequest;
import com.security.spring.gamesoft.callback.dto.PushBetResponse;

public interface PushBetService {
     PushBetResponse pushbetConfig(PushBetRequest data);
}
