package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.thirdpartygames.callback.dto.CallBackRequest;
import com.security.spring.thirdpartygames.callback.dto.Response;
import com.security.spring.thirdpartygames.callback.dto.TransactionCallBackResponse;

public interface PlaceBetService {
    Response<TransactionCallBackResponse> placebetConfig(CallBackRequest data);
}
