package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.exceptionall.ApiDuplicateTransaction;
import com.security.spring.exceptionall.BetNotExistsException;
import com.security.spring.thirdpartygames.callback.dto.Action;
import com.security.spring.thirdpartygames.callback.dto.BatchRequest;
import com.security.spring.thirdpartygames.callback.dto.CallBackRequest;
import com.security.spring.thirdpartygames.callback.dto.Response;
import com.security.spring.thirdpartygames.callback.dto.TransactionCallBackResponse;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.transaction.entity.GameSoftTransaction;
import com.security.spring.thirdpartygames.transaction.repsitory.GameSoftTransactionRepo;
import com.security.spring.thirdpartygames.wager.repository.GameSoftWagerReop;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.ErrorMessageUtil;
import com.security.spring.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallBackDepositServiceImpl implements CallBackDepositService {

    private final UserRepository userRepository;
    private final GameSoftWagerReop gameSoftWagerReop;
    private final GameSoftTransactionRepo gameSoftTransactionRepo;
    private final CallbackServiceHelper callbackServiceHelper;

    @Override
    public Response<TransactionCallBackResponse> depositConfig(CallBackRequest data) {

        log.info("Deposit : {}", data.toString());
        callbackServiceHelper.verifySignature(data, "deposit");
        callbackServiceHelper.validateCurrency(data.getCurrency());

        List<TransactionCallBackResponse> callBackResponseList = new LinkedList<>();
        long beforeBalance = 0;
        long balance = 0;
        for (BatchRequest batchRequest : data.getBatchRequests()) {
            User user = callbackServiceHelper.findUser(batchRequest.getMemberAccount());
            GameType gameType = callbackServiceHelper.findGameType(batchRequest.getGameType());
            GameSoftGameProvider gameProvider = callbackServiceHelper.findGameProvider(batchRequest.getProductCode(), gameType);

            for (var transaction : batchRequest.getTransactions()) {
                callbackServiceHelper.validateAction(transaction.getAction());

                if (gameSoftTransactionRepo.findByTransactionId(transaction.getTransactionId()).isPresent()) {
                    throw new ApiDuplicateTransaction(ErrorMessageUtil.API_DUPLICATE_TRANSACTION);
                }
                UserUnits userUnits = user.getUserUnits();
                beforeBalance = (long) userUnits.getMainUnit();
                GameSoftTransaction transitionObj;

                balance = (long) (beforeBalance + transaction.getAmount());
                userUnits.setMainUnit(balance);
                if (transaction.getAction().equals(Action.CANCEL.name())) {
                    var wager = gameSoftWagerReop.findByWagerID(transaction.getWagerCode());
                    if (wager.isEmpty()) {
                        throw new BetNotExistsException(ErrorMessageUtil.API_BET_NOT_EXIST);
                    }
                    transitionObj = MapperUtil.mapToTransaction(transaction, gameType, gameProvider, user, wager.get(), beforeBalance, balance);
                } else {
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
                    .memberAccount(user.getAr7Id())
                    .productCode(batchRequest.getProductCode())
                    .build());
        }
        Response<TransactionCallBackResponse> response = new Response<>();
        response.setData(callBackResponseList);
        return response;
    }
}
