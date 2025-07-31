package com.security.spring.gamesoft.callback.service;

import com.security.spring.exceptionall.ApiMemberDoesNotExist;
import com.security.spring.exceptionall.CurrencyDoesNotSupportException;
import com.security.spring.exceptionall.InvalidWrongSignException;
import com.security.spring.gamesoft.callback.dto.*;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.CurrencyUtil;
import com.security.spring.utils.ErrorMessageUtil;
import com.security.spring.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetBalanceServiceImpl implements GetBalanceService{


    private final UserRepository userRepository;


    @Override
    public Response<GetBalanceCallBackResponse> getBalanceService(CallBackRequest data) {
        log.info("Get Balance : {}", data.toString());
        ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();
        if (!SignUtil.verifySignature(data.getOperatorCode(), data.getRequestTime(), "getbalance", constantDataObj.getSecretKey(), data.getSign())) {
            throw new InvalidWrongSignException(ErrorMessageUtil.API_INVALID_SIGN);
        }

        if (Arrays.stream(Currency.values()).noneMatch(e->e.name().equals(data.getCurrency()))){
            throw new CurrencyDoesNotSupportException("Invalid Currency");
        }

        List<GetBalanceCallBackResponse> getBalanceCallBackResponseList = new LinkedList<>();

        for (BatchRequest batchRequest : data.getBatchRequests()) {
            String tripleId = batchRequest.getMemberAccount();
            Optional<User> currentUser = userRepository.findByAr7Id(tripleId);
            if (currentUser.isEmpty()) {
                throw new ApiMemberDoesNotExist(ErrorMessageUtil.API_MEMBER_NOT_EXISTS);
            }

            BigDecimal mainUnit = BigDecimal.valueOf(currentUser.get().getUserUnits().getMainUnit());
            String currencyCode = data.getCurrency();
            BigDecimal rate = CurrencyUtil.getCurrencyRate(currencyCode);

            // Perform multiplication and convert to long
            BigDecimal scaledBalance = mainUnit.multiply(rate).setScale(4, RoundingMode.HALF_UP);
            long balance = scaledBalance.longValue();  // 4 decimal shift




            getBalanceCallBackResponseList.add(GetBalanceCallBackResponse
                    .builder()
                    .code(0)
                    .message("success")
                    .balance(balance)
                    .memberAccount(tripleId)
                    .productCode(batchRequest.getProductCode())
                    .build());
        }
        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(getBalanceCallBackResponseList);
        return response;
    }


}
