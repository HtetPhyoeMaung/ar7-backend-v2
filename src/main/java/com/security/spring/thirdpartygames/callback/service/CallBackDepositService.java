package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.thirdpartygames.callback.dto.CallBackRequest;
import com.security.spring.thirdpartygames.callback.dto.Response;
import com.security.spring.thirdpartygames.callback.dto.TransactionCallBackResponse;

public interface CallBackDepositService {
    Response<TransactionCallBackResponse> depositConfig(CallBackRequest data);
}
