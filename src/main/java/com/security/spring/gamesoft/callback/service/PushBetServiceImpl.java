package com.security.spring.gamesoft.callback.service;

import com.security.spring.exceptionall.InvalidWrongSignException;
import com.security.spring.exceptionall.PushBetException;
import com.security.spring.gamesoft.callback.dto.PushBetRequest;
import com.security.spring.gamesoft.callback.dto.PushBetResponse;
import com.security.spring.gamesoft.callback.dto.WagerRequest;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameType.service.GameTypeService;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.gameprovider.service.GameSoftGameProviderService;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.gamesoft.wager.repository.GameSoftWagerReop;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PushBetServiceImpl implements PushBetService{
    private final UserRepository userRepository;
    private final GameTypeService gameTypeService;
    private final GameSoftGameProviderService gameSoftGameProviderService;
    private final CommonUtil commonUtil;
    private final GameSoftWagerReop gameSoftWagerReop;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public PushBetResponse pushbetConfig(PushBetRequest data) {
        ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();
        if (!SignUtil.verifySignature(data.getOperatorCode(), data.getRequestTime(), "pushbetdata", constantDataObj.getSecretKey(), data.getSign())) {
            throw new InvalidWrongSignException(ErrorMessageUtil.API_INVALID_SIGN);
        }

        for (WagerRequest wagerRequest : data.getWagerRequestList()) {
            Optional<User> currentUser = userRepository.findByAr7Id(wagerRequest.getMemberAccount());
            if (currentUser.isEmpty()) {
                throw new PushBetException(ErrorMessageUtil.API_MEMBER_NOT_EXISTS);
            }
            User user = currentUser.get();
            UserUnits currentUnit = user.getUserUnits();


            GameType gameType =gameTypeService.findByCode(wagerRequest.getGameType());

            GameSoftGameProvider provider = gameSoftGameProviderService.findByProductAndGameType(Long.valueOf(wagerRequest.getProductCode()),gameType);

            double beforeBalance = 0;
            double balance = currentUnit.getMainUnit();

            GameSoftWager gameSoftWager = objectMapper.mapToWager(wagerRequest,gameType,provider,user);

            gameSoftWagerReop.save(gameSoftWager);

        }
        return PushBetResponse.builder()
                .code(ResponseCode.SUCCESS)
                .message("Success")
                .build();
    }
}
