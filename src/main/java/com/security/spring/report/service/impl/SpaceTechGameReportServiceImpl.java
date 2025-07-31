package com.security.spring.report.service.impl;

import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.transaction.repsitory.GameSoftTransactionRepo;
import com.security.spring.report.dto.ExitInfosObj;
import com.security.spring.report.dto.SpaceTechReportObj;
import com.security.spring.report.dto.SpaceTechGameReportResponse;
import com.security.spring.report.repo.SpaceTechGameReportRepo;
import com.security.spring.report.service.ShanReportService;
import com.security.spring.spacetechmm.entity.SpaceTechGame;
import com.security.spring.spacetechmm.repo.SpaceTechRepository;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.unit.repo.UnitRepository;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceTechGameReportServiceImpl implements ShanReportService {

    private final SpaceTechGameReportRepo spaceTechGameReportRepo;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final UserService userService;
    private final SpaceTechRepository spaceTechRepository;
    private final GameSoftTransactionRepo gameSoftTransactionRepo;

    @Override
    public SpaceTechGameReportResponse saveSpaceTechGameReport(SpaceTechReportObj spaceTechReportObj) {
        log.info("Space Tech Game Report Obj : {}", spaceTechReportObj);
        List<SpaceTechGame> spaceTechGameList =spaceTechRepository.findByGameNameContainingIgnoreCase(spaceTechReportObj.getGame());
        if (spaceTechGameList.isEmpty()){
            new DataNotFoundException("Space Tech Game not found by Game Name : "+spaceTechReportObj.getGame());
        }

        if (!spaceTechReportObj.getTransactions().isEmpty()) {
            for (var transaction : spaceTechReportObj.getTransactions()) {


                String ar7Id = transaction.getId();

                User user = userRepository.findByAr7Id(ar7Id).orElseThrow(() ->
                        new DataNotFoundException("User not found by ar7Id : " + ar7Id));
                UserUnits userUnits = user.getUserUnits();
                double beforeBalance = userUnits.getMainUnit();
//                userUnits.setMainUnit(userUnits.getMainUnit() + transaction.getAmount());
                double afterBalance = userUnits.getMainUnit() + transaction.getAmount();
                GameSoftTransaction gameSoftTransaction = GameSoftTransaction.builder()
                        .gameSoftTransitionUser(userService.findByAr7Id(transaction.getId()))
                        .betAmount(transaction.getBet())
                        .amount(transaction.getAmount())
                        .gameType(spaceTechGameList.get(0).getGameType())
                        .isCommissionCalculate(false)
                        .status("SETTLED")
                        .beforeBalance(beforeBalance)
                        .afterBalance(afterBalance)
                        .createdOn(LocalDateTime.now())
                        .modifiedOn(LocalDateTime.now())
                        .commisionAmount(transaction.getCommission())
                        .build();
                gameSoftTransaction = gameSoftTransactionRepo.save(gameSoftTransaction);

//                unitRepository.save(userUnits);

            }
        }

        if(spaceTechReportObj.getExitInfos() != null && !spaceTechReportObj.getExitInfos().isEmpty()){
            for(ExitInfosObj exitInfosObj : spaceTechReportObj.getExitInfos()){
                String ar7Id = exitInfosObj.getId();

                UserUnits userUnits = unitRepository.findByUser_Ar7Id(ar7Id).orElseThrow(() -> new DataNotFoundException("User not found by ar7Id"));
                userUnits.setMainUnit(userUnits.getMainUnit() + exitInfosObj.getBalance());
                unitRepository.save(userUnits);
            }
        }



        return SpaceTechGameReportResponse.builder()
                .status("ok")
                .build();

    }
}
