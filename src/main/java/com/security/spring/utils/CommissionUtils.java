package com.security.spring.utils;

import com.security.spring.commission.dto.CommissionObj;
import com.security.spring.commission.entity.UserCommission;
import com.security.spring.commission.repo.UserCommissionRepo;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class CommissionUtils {
    private final UserCommissionRepo userCommissionRepo;
    private final UserService userService;

    public  List<CommissionObj> calculateCommission(List<GameSoftTransaction> gameSoftTransactionList, List<UserCommission> commissionList) {
       return gameSoftTransactionList
                .stream()
                .map(

                        obj -> {
                            double matchingCommission = commissionList.stream()
                                    .filter(commission -> commission.getGameType().getCode().equals(obj.getGameType().getCode()))
                                    .findFirst().get().getCommission();
                            String agentAr7Id = obj.getGameSoftTransitionUser().getParentUserId();
                            double agentCommissionPercentage = userCommissionRepo.findByUser_ar7IdAndGameType_code(agentAr7Id,obj.getGameType().getCode()).getCommission();

                            String masterAr7Id = userService.findByAr7Id(agentAr7Id).getParentUserId();
                            double masterCommissionPercentage = userCommissionRepo.findByUser_ar7IdAndGameType_code(masterAr7Id,obj.getGameType().getCode()).getCommission()-agentCommissionPercentage;

                            String seniorMasterAr7Id = userService.findByAr7Id(masterAr7Id).getParentUserId();
                            double seniorMasterCommissionPercentage = userCommissionRepo.findByUser_ar7IdAndGameType_code(seniorMasterAr7Id,obj.getGameType().getCode()).getCommission() - (masterCommissionPercentage+agentCommissionPercentage);

                            log.info("Master Commission Percentage : {}",masterCommissionPercentage);
                            double winAmt = obj.getAmount() >= 0 ? obj.getAmount() : 0.0;
                            double upLineWinLose = obj.getBetAmount() - winAmt;
                            double agentCommission = (obj.getBetAmount() - winAmt) / 100 * agentCommissionPercentage;
                            double masterCommission = (obj.getBetAmount() - winAmt) / 100 * masterCommissionPercentage;
                            double seniorMasterCommission = (obj.getBetAmount() - winAmt) / 100 * seniorMasterCommissionPercentage;


                            return CommissionObj
                                    .builder()
                                    .ar7Id(obj.getGameSoftTransitionUser().getAr7Id())
                                    .betAmount(obj.getBetAmount())
                                    .winAmount(winAmt)
                                    .winLoseAmount(winAmt - obj.getBetAmount())
                                    .agCommission(agentCommission)
                                    .msCommission(masterCommission)
                                    .seCommission(seniorMasterCommission)
                                    .msCommissionPercentage(masterCommissionPercentage)
                                    .gameTypeCode(obj.getGameType().getCode())
                                    .upLineWinLose(upLineWinLose)
                                    .agCommissionPercentage(agentCommissionPercentage)
                                    .seCommissionPercentage(seniorMasterCommissionPercentage)
                                    .gameTypeName(obj.getGameType().getDescription())
                                    .build();
                        }
                )
                .toList();
    }
}
