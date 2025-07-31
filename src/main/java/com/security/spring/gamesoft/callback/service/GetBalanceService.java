package com.security.spring.gamesoft.callback.service;

import com.security.spring.gamesoft.callback.dto.CallBackRequest;
import com.security.spring.gamesoft.callback.dto.GetBalanceCallBackResponse;
import com.security.spring.gamesoft.callback.dto.Response;

public interface GetBalanceService {
     Response<GetBalanceCallBackResponse> getBalanceService(CallBackRequest data);
}
