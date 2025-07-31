package com.security.spring.utils;

import com.security.spring.gamesoft.callback.dto.CallBackTransaction;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.notification.dto.NotificationResponse;
import com.security.spring.notification.entity.Notification;
import com.security.spring.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
@Component

public class MapperUtil {

    private MapperUtil(){

    }

    public static GameSoftTransaction mapToTransaction(CallBackTransaction request, GameType gameType, GameSoftGameProvider product, User user,GameSoftWager wager, double beforeBalance,
                                                       double afterBalance){
        return GameSoftTransaction.builder()
                .gameCode(request.getGameCode())
                .roundId(request.getRoundId())
                .status(request.getWagerStatus())
                .betAmount(request.getBetAmount())
                .currency(request.getCurrency())
                .payload(request.getPayload() ==null?"":request.getPayload().toString())
                .betAmount(request.getBetAmount())
                .transactionId(request.getTransactionId())
                .productID(product)
                .validBetAmount(request.getValidBetAmount())
                .productID(product)
                .settleAt(request.getSettleAt())
                .gameSoftTransitionUser(user)
                .wagerCode(wager==null?null:wager.getWagerCode())
                .gameType(gameType)
                .gameSoftWager(wager)
                .amount(request.getAmount())
                .tipAmount(request.getTipAmount())
                .prizeAmount(request.getPrizeAmount())
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static NotificationResponse mapToNotificationResponse(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .senderId(notification.getSenderId())
                .receiverId(notification.getReceiverId())
                .createdTime(notification.getCreatedTime())
                .updatedTime(notification.getUpdatedTime())
                .build();
    }


    public static GameSoftWager mapToWager(CallBackTransaction requestObj, GameType gameType, GameSoftGameProvider gameProvider, User updatedUser) {
        return GameSoftWager
                .builder()
                .wagerID(requestObj.getWagerCode())
                .gameSoftWagerUser(updatedUser)
                .provider(gameProvider)
                .gameTypeId(gameType)
                .currencyID(requestObj.getCurrency())
                .gameCode(requestObj.getGameCode())
                .gameRoundID(requestObj.getRoundId())
                .validBetAmount(requestObj.getValidBetAmount())
                .betAmount(requestObj.getBetAmount())
                .prizeAmount(requestObj.getPrizeAmount())
                .payload(requestObj.getPayload().toString())
                .status(requestObj.getWagerStatus())
                .settlementDate(requestObj.getSettleAt())
                .createdOn(LocalDateTime.now())
                .build();

    }

    public static String getTransactionKey(String transactionID) {
        if (transactionID != null) {
            return transactionID.substring(11).trim();
        }
        return "";
    }

    public static ZonedDateTime parseToUTC00Safely(String dateStr) {
        try {
            return ZonedDateTime.parse(dateStr).withZoneSameInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date format. Expected ISO-8601 format: " + dateStr, e);
        }
    }


}
