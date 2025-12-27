package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.thirdpartygames.callback.dto.*;
import com.security.spring.user.entity.User;
import com.security.spring.utils.CurrencyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetBalanceServiceImpl implements GetBalanceService {

    private final CallbackServiceHelper callbackServiceHelper;

    @Override
    public Response<GetBalanceCallBackResponse> getBalanceService(CallBackRequest data) {
        log.info("Get Balance: {}", data.toString());
        callbackServiceHelper.verifySignature(data, "getbalance");
        callbackServiceHelper.validateCurrency(data.getCurrency());

        List<GetBalanceCallBackResponse> getBalanceCallBackResponseList = new LinkedList<>();
        for (BatchRequest batchRequest : data.getBatchRequests()) {
            User currentUser = callbackServiceHelper.findUser(batchRequest.getMemberAccount());

            BigDecimal mainUnit = BigDecimal.valueOf(currentUser.getUserUnits().getMainUnit());
            String currencyCode = data.getCurrency();
            BigDecimal rate = CurrencyUtil.getCurrencyRate(currencyCode);

            // Perform multiplication and convert to long
            BigDecimal scaledBalance = mainUnit.multiply(rate).setScale(4, RoundingMode.HALF_UP);
            long balance = scaledBalance.longValue();

            getBalanceCallBackResponseList.add(GetBalanceCallBackResponse.builder()
                    .code(0)
                    .message("success")
                    .balance(balance)
                    .memberAccount(batchRequest.getMemberAccount())
                    .productCode(batchRequest.getProductCode())
                    .build());
        }

        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(getBalanceCallBackResponseList);
        return response;
    }
}
