package com.security.spring.thirdpartygames.callback.service;

import com.security.spring.exceptionall.ApiDuplicateTransaction;
import com.security.spring.exceptionall.InsufficientBalanceException;
import com.security.spring.thirdpartygames.callback.dto.Action;
import com.security.spring.thirdpartygames.callback.dto.BatchRequest;
import com.security.spring.thirdpartygames.callback.dto.CallBackRequest;
import com.security.spring.thirdpartygames.callback.dto.Response;
import com.security.spring.thirdpartygames.callback.dto.TransactionCallBackResponse;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.transaction.entity.GameSoftTransaction;
import com.security.spring.thirdpartygames.transaction.repsitory.GameSoftTransactionRepo;
import com.security.spring.thirdpartygames.wager.entity.GameSoftWager;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.CommonUtil;
import com.security.spring.utils.ErrorMessageUtil;
import com.security.spring.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceBetServiceImpl implements PlaceBetService {

    private final UserRepository userRepository;
    private final GameSoftTransactionRepo gameSoftTransactionRepo;
    private final CommonUtil commonUtil;
    private final CallbackServiceHelper callbackServiceHelper;

    @Override
    @Transactional
    public Response<TransactionCallBackResponse> placebetConfig(CallBackRequest data) {

        log.info("Withdraw : {}", data.toString());
        callbackServiceHelper.verifySignature(data, "withdraw");
        callbackServiceHelper.validateCurrency(data.getCurrency());

        List<TransactionCallBackResponse> getBalanceCallBackResponseList = new LinkedList<>();
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

                if (Math.abs(transaction.getAmount()) > beforeBalance) {
                    throw new InsufficientBalanceException(ErrorMessageUtil.API_MEMBER_INSUFFICIENT_BALANCE, user.getAr7Id());
                }
                balance = (long) (beforeBalance + transaction.getAmount());
                userUnits.setMainUnit(balance);

                GameSoftWager gameSoftWager = commonUtil.updateOrSaveWager(transaction, gameType, gameProvider, user);

                GameSoftTransaction transitionObj = MapperUtil.mapToTransaction(transaction, gameType, gameProvider, user, gameSoftWager, beforeBalance, balance);
                if (transitionObj.getStatus().equals(Action.BET.name())) {
                    if (userUnits.getTurnAmount() < transaction.getValidBetAmount()) {
                        userUnits.setTurnAmount(0);
                    } else {
                        userUnits.setTurnAmount(userUnits.getTurnAmount() - transaction.getValidBetAmount());
                    }
                    userUnits.setTotalBetUnit(userUnits.getTotalBetUnit() + transaction.getValidBetAmount());
                }
                gameSoftTransactionRepo.save(transitionObj);
            }
            userRepository.save(user);

            getBalanceCallBackResponseList.add(TransactionCallBackResponse
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
        response.setData(getBalanceCallBackResponseList);
        return response;
    }
}
