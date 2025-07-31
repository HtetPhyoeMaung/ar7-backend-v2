package com.security.spring.utils;

import com.security.spring.ads.dto.AdsResponse;
import com.security.spring.ads.entity.Ads;
import com.security.spring.bank.bankName.dto.BankNameAuthObj;
import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import com.security.spring.bank.bankName.repo.BankNameRepo;
import com.security.spring.bank.bankType.dto.BankTypeAuthObj;
import com.security.spring.bank.bankType.dto.BankTypeObj;
import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.bank.bankType.entity.BankTypeAuth;
import com.security.spring.commission.dto.GameTypeCommissionObj;
import com.security.spring.commission.entity.UserCommission;
import com.security.spring.gamesoft.callback.dto.Currency;
import com.security.spring.gamesoft.callback.dto.WagerRequest;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.spacetechmm.dto.SpaceTechGameDto;
import com.security.spring.spacetechmm.entity.SpaceTechGame;
import com.security.spring.storage.StorageService;
import com.security.spring.term_and_condition.dto.TermAndConditionDto;
import com.security.spring.term_and_condition.entity.TermAndCondition;
import com.security.spring.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ObjectMapper {
    private final StorageService storageService;
    private final BankNameRepo bankNameRepo;

    public SpaceTechGameDto mapToSpaceTechGameDto(SpaceTechGame obj) {
        return SpaceTechGameDto.builder()
                .gameName(obj.getGameName())
                .gameCode(obj.getGameCode())
                .id(obj.getId())
                .gameTypeId(Long.valueOf(obj.getGameType().getId()))
                .gameTypeName(obj.getGameType().getDescription())
                .imageUrl(obj.getImageName()==null?null:storageService.getImageByName(obj.getImageName()))
                .build();
    }

    public BankTypeObj mapToBankTypeObj(BankType responseObj) {
        return BankTypeObj.builder()
                .bankTypeId(responseObj.getId())
                .bankTypeName(responseObj.getBankTypeName())
                .build();
    }


    public BankTypeAuthObj mapToBankTypeAuthObj(BankTypeAuth bankTypeAuth) {
        return BankTypeAuthObj.builder()
                .id(bankTypeAuth.getId())
                .availableBankTypeName(bankTypeAuth.getBankType().getBankTypeName())
                .availableBankTypeId(bankTypeAuth.getBankType().getId())
                .bankTypeStatus(bankTypeAuth.isBankTypeStatus())
                .initialStatus(bankTypeAuth.getInitStatus())
                .build();
    }

    public GameTypeCommissionObj mapToCommissionObj(UserCommission userCommission) {
        return GameTypeCommissionObj.builder()
                .id(userCommission.getId())
                .ar7Id(userCommission.getUser().getAr7Id())
                .parentUserId(userCommission.getUpLineAr7Id())
                .gameType(userCommission.getGameType().getDescription())
                .commission(String.valueOf(userCommission.getCommission()))
                .build();
    }
    public GameSoftWager mapToWager(WagerRequest wagerRequest, GameType gameType, GameSoftGameProvider provider, User currentUser) {
        return GameSoftWager.builder()
                .wagerID(wagerRequest.getWagerCode())
                .wagerCode(wagerRequest.getWagerCode())
                .gameSoftWagerUser(currentUser)
                .gameRoundID(wagerRequest.getRoundId())
                .currencyID(Currency.valueOf(wagerRequest.getCurrency()))
                .provider(provider)

                .betAmount(Double.parseDouble(wagerRequest.getBetAmount()))
                .validBetAmount(Double.parseDouble(wagerRequest.getValidBetAmount()))
                .prizeAmount(Double.parseDouble(wagerRequest.getPrizeAmount()))

                .status(wagerRequest.getWagerStatus())
                .createdAt(wagerRequest.getCreatedAt())
                .createdOn(LocalDateTime.now())
                .payload(wagerRequest.getPayload()==null?"":wagerRequest.getPayload().toString())
                .build();
    }

    public BankNameAuthObj mapToBankNameAuthObj(BankNameAuth bankNameAuth) {
        BankName bankName = bankNameAuth.getBankName();
        return BankNameAuthObj.builder()
                .bankNameId(bankNameAuth.getBankName().getId())
                .bankNameStatus(bankNameAuth.getBankNameStatus())
                .bankName(bankNameAuth.getBankName().getBankName())
                .bankTypeId(bankNameAuth.getBankType().getId())
                .bankTypeName(bankNameAuth.getBankType().getBankTypeName())
                .initialStatus(bankNameAuth.getInitialStatus())
                .id(bankNameAuth.getId())
                .bankNameImageUrl(bankName.getBankNameImageUrl())
                .build();
    }

    public TermAndConditionDto mapToTermAndConditionDto(TermAndCondition termAndCondition) {
        return TermAndConditionDto.builder()
                .id(termAndCondition.getId())
                .context(termAndCondition.getContext())
                .termAndConditionType(termAndCondition.getType())
                .build();
    }

    public AdsResponse mapToAdsResponse(Ads ads) {
        return AdsResponse.builder()
                .id(ads.getId())
                .text(ads.getText()==null?null:ads.getText())
                .imageUrl(ads.getImageName()==null?null:storageService.getImageByName(ads.getImageName()))
                .type(ads.getAdsType())
                .build();
    }
}
