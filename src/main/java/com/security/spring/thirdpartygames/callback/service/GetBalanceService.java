package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.thirdpartygames.callback.dto.CallBackRequest;
import com.security.spring.thirdpartygames.callback.dto.GetBalanceCallBackResponse;
import com.security.spring.thirdpartygames.callback.dto.Response;

public interface GetBalanceService {
     Response<GetBalanceCallBackResponse> getBalanceService(CallBackRequest data);
}
