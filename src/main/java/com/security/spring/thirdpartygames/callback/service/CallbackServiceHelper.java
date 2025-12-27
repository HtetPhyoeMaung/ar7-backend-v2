package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.exceptionall.ApiMemberDoesNotExist;
import com.security.spring.exceptionall.CurrencyDoesNotSupportException;
import com.security.spring.exceptionall.InvalidWrongSignException;
import com.security.spring.thirdpartygames.callback.dto.Action;
import com.security.spring.thirdpartygames.callback.dto.CallBackRequest;
import com.security.spring.thirdpartygames.callback.dto.Currency;
import com.security.spring.thirdpartygames.callback.dto.PushBetRequest;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameType.service.GameTypeService;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.gameprovider.service.GameSoftGameProviderService;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.ErrorMessageUtil;
import com.security.spring.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CallbackServiceHelper {

    private final UserRepository userRepository;
    private final GameTypeService gameTypeService;
    private final GameSoftGameProviderService gameSoftGameProviderService;
    private final ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();

    public void verifySignature(CallBackRequest request, String methodName) {
        if (!SignUtil.verifySignature(request.getOperatorCode(), request.getRequestTime(), methodName, constantDataObj.getSecretKey(), request.getSign())) {
            throw new InvalidWrongSignException(ErrorMessageUtil.API_INVALID_SIGN);
        }
    }

    public void verifySignature(PushBetRequest request, String methodName) {
        if (!SignUtil.verifySignature(request.getOperatorCode(), request.getRequestTime(), methodName, constantDataObj.getSecretKey(), request.getSign())) {
            throw new InvalidWrongSignException(ErrorMessageUtil.API_INVALID_SIGN);
        }
    }

    public void validateCurrency(String currency) {
        if (Arrays.stream(Currency.values()).noneMatch(e -> e.name().equals(currency))) {
            throw new CurrencyDoesNotSupportException("Invalid Currency");
        }
    }

    public void validateAction(String action) {
        if (Arrays.stream(Action.values()).noneMatch(e -> e.name().equals(action))) {
            throw new CurrencyDoesNotSupportException("Invalid Action");
        }
    }

    public User findUser(String memberAccount) {
        return userRepository.findByAr7Id(memberAccount)
                .orElseThrow(() -> new ApiMemberDoesNotExist(ErrorMessageUtil.API_MEMBER_NOT_EXISTS));
    }

    public GameType findGameType(String gameTypeCode) {
        return gameTypeService.findByCode(gameTypeCode);
    }

    public GameSoftGameProvider findGameProvider(int productCode, GameType gameType) {
        return gameSoftGameProviderService.findByProductAndGameType((long) productCode, gameType);
    }

    public GameSoftGameProvider findGameProvider(long productCode, GameType gameType) {
        return gameSoftGameProviderService.findByProductAndGameType(productCode, gameType);
    }
}
