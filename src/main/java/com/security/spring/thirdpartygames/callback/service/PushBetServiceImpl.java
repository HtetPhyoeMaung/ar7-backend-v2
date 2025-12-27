package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.exceptionall.PushBetException;
import com.security.spring.thirdpartygames.callback.dto.PushBetRequest;
import com.security.spring.thirdpartygames.callback.dto.PushBetResponse;
import com.security.spring.thirdpartygames.callback.dto.WagerRequest;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.wager.entity.GameSoftWager;
import com.security.spring.thirdpartygames.wager.repository.GameSoftWagerReop;
import com.security.spring.user.entity.User;
import com.security.spring.utils.ErrorMessageUtil;
import com.security.spring.utils.ObjectMapper;
import com.security.spring.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PushBetServiceImpl implements PushBetService {

    private final GameSoftWagerReop gameSoftWagerReop;
    private final ObjectMapper objectMapper;
    private final CallbackServiceHelper callbackServiceHelper;

    @Override
    @Transactional
    public PushBetResponse pushbetConfig(PushBetRequest data) {
        callbackServiceHelper.verifySignature(data, "pushbetdata");

        for (WagerRequest wagerRequest : data.getWagerRequestList()) {
            User user = callbackServiceHelper.findUser(wagerRequest.getMemberAccount());
            if (user == null) {
                throw new PushBetException(ErrorMessageUtil.API_MEMBER_NOT_EXISTS);
            }

            GameType gameType = callbackServiceHelper.findGameType(wagerRequest.getGameType());
            GameSoftGameProvider provider = callbackServiceHelper.findGameProvider(Long.parseLong(wagerRequest.getProductCode()), gameType);

            GameSoftWager gameSoftWager = objectMapper.mapToWager(wagerRequest, gameType, provider, user);
            gameSoftWagerReop.save(gameSoftWager);
        }

        return PushBetResponse.builder()
                .code(ResponseCode.SUCCESS)
                .message("Success")
                .build();
    }
}
