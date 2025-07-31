package com.security.spring.gamesoft.callback.service;

import com.security.spring.gamesoft.callback.dto.CallBackRequest;
import com.security.spring.gamesoft.callback.dto.Response;
import com.security.spring.gamesoft.callback.dto.TransactionCallBackResponse;

public interface CallBackDepositService {
    Response<TransactionCallBackResponse> depositConfig(CallBackRequest data);
}
