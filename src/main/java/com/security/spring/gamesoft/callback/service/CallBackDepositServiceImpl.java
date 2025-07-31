package com.security.spring.gamesoft.callback.service;

import com.security.spring.exceptionall.*;
import com.security.spring.gamesoft.callback.dto.*;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameType.service.GameTypeService;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.gameprovider.service.GameSoftGameProviderService;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.transaction.repsitory.GameSoftTransactionRepo;
import com.security.spring.gamesoft.wager.repository.GameSoftWagerReop;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallBackDepositServiceImpl implements CallBackDepositService {


    private final UserRepository userRepository;
    private final GameSoftWagerReop gameSoftWagerReop;
    private final GameSoftTransactionRepo gameSoftTransactionRepo;
    private final GameTypeService gameTypeService;
    private final GameSoftGameProviderService gameSoftGameProviderService;
    private final CommonUtil commonUtil;

    @Override
    public Response<TransactionCallBackResponse> depositConfig(CallBackRequest data) {

        log.info("Deposit : {}", data.toString());
        ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();
        if (!SignUtil.verifySignature(data.getOperatorCode(), data.getRequestTime(), "deposit", constantDataObj.getSecretKey(), data.getSign())) {
            throw new InvalidWrongSignException(ErrorMessageUtil.API_INVALID_SIGN);
        }

        if (Arrays.stream(Currency.values()).noneMatch(e->e.name().equals(data.getCurrency()))){
            throw new CurrencyDoesNotSupportException("Invalid Currency");
        }


        List<TransactionCallBackResponse> callBackResponseList = new LinkedList<>();
        long beforeBalance = 0;
        long balance = 0;
        for (BatchRequest batchRequest : data.getBatchRequests()) {
            String tripleId = batchRequest.getMemberAccount();
            Optional<User> currentUser = userRepository.findByAr7Id(tripleId);
            if (currentUser.isEmpty()) {
                throw new ApiMemberDoesNotExist(ErrorMessageUtil.API_MEMBER_NOT_EXISTS);
            }

            GameType gameType = gameTypeService.findByCode(batchRequest.getGameType());
            GameSoftGameProvider gameProvider = gameSoftGameProviderService.findByProductAndGameType((long) batchRequest.getProductCode(), gameType);
            User user = currentUser.get();
            for (var transaction : batchRequest.getTransactions()) {
                if (Arrays.stream(Action.values()).noneMatch(e->e.name().equals(transaction.getAction()))){
                    throw new CurrencyDoesNotSupportException("Invalid Action");
                }
                if (gameSoftTransactionRepo.findByTransactionId(transaction.getTransactionId()).isPresent()) {
                    throw new ApiDuplicateTransaction(ErrorMessageUtil.API_DUPLICATE_TRANSACTION);
                }
                UserUnits userUnits = user.getUserUnits();
                 beforeBalance = (long) userUnits.getMainUnit();
                GameSoftTransaction transitionObj;

                balance = (long) (beforeBalance + transaction.getAmount());
                userUnits.setMainUnit(balance);
                 if (transaction.getAction().equals(Action.CANCEL.name())){
                     var wager = gameSoftWagerReop.findByWagerID(transaction.getWagerCode());
                 if (wager.isEmpty()){
                     throw new BetNotExistsException(ErrorMessageUtil.API_BET_NOT_EXIST);
                 }
                     transitionObj = MapperUtil.mapToTransaction(transaction, gameType, gameProvider, user, wager.get(), beforeBalance, balance);
                 }else {
                     transitionObj = MapperUtil.mapToTransaction(transaction, gameType, gameProvider, user, null, beforeBalance, balance);
                 }

                gameSoftTransactionRepo.save(transitionObj);



            }
            userRepository.save(user);

            callBackResponseList.add(TransactionCallBackResponse
                    .builder()
                    .code(0)
                    .message("success")
                    .balance(balance)
                    .beforeBalance(beforeBalance)
                    .memberAccount(tripleId)
                    .productCode(batchRequest.getProductCode())
                    .build());
        }
        Response<TransactionCallBackResponse> response = new Response<>();
        response.setData(callBackResponseList);
        return response;
    }
}
