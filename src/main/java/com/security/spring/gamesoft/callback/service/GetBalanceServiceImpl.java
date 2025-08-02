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
            // Assuming CurrencyUtil.getCurrencyRate(currencyCode) correctly fetches the rate from the Currency enum
            BigDecimal rate = CurrencyUtil.getCurrencyRate(currencyCode);

            // FIX: Perform division by the rate and then scale for the long representation.
            // The error message "Expected 1/1000 conversion with 4 decimal places" implies:
            // 1. The 'mainUnit' needs to be divided by the 'rate' (e.g., 1000 for MMK2).
            // 2. The resulting decimal value should be formatted to 4 decimal places.
            // 3. For the 'long balance' field, this decimal value must be multiplied by 10^4 (10000)
            //    to implicitly carry the 4 decimal places as an integer.
            BigDecimal actualConvertedBalance = mainUnit.divide(rate, 4, RoundingMode.HALF_UP);

            // Convert the decimal value to a long by shifting the decimal point 4 places to the right
            // (multiplying by 10000) to preserve the 4 decimal places as an integer.
            long balance = actualConvertedBalance.multiply(BigDecimal.valueOf(10000)).longValue();

            getBalanceCallBackResponseList.add(GetBalanceCallBackResponse
                    .builder()
                    .code(0)
                    .message("success")
                    .balance(balance) // This 'balance' is now the correctly scaled long value
                    .memberAccount(tripleId)
                    .productCode(batchRequest.getProductCode())
                    .build());
        }

        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(getBalanceCallBackResponseList);
        return response;
    }


}
