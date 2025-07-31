package com.security.spring.report.service.impl;

import com.security.spring.commission.entity.CommissionConfirm;
import com.security.spring.commission.repo.CommissionConfirmRepo;
import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.transaction.repsitory.GameSoftTransactionRepo;
import com.security.spring.report.dto.*;
import com.security.spring.report.service.UserDetailReportService;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.Constraint;
import com.security.spring.utils.DateUitls;
import com.security.spring.utils.GameTransactionGroupKey;
import com.security.spring.utils.UserPlayDetailTransitionGroupKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailReportServiceImpl implements UserDetailReportService {

    private final GameSoftTransactionRepo gameSoftTransactionRepo;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final CommissionConfirmRepo commissionConfirmRepo;

    @Override
    public ResponseEntity<UserDetailsResponse> getUserDetailReportByAr7Id(String ar7Id, LocalDateTime startDate, LocalDateTime  endDate) {


        User user = userRepository.findByAr7Id(ar7Id).orElseThrow(() ->
                new DataNotFoundException("User not found by " + ar7Id));
        List<GameSoftTransaction> gameSoftTransactionPage;
        if (startDate==null && endDate==null){
            gameSoftTransactionPage = gameSoftTransactionRepo.findByGameSoftTransitionUser(user);
        }else {

            gameSoftTransactionPage = gameSoftTransactionRepo.findByGameSoftTransitionUserAndWagerStatusAndCreatedOnBetween(user,"SETTLED",startDate, endDate);
        }

        List<UserReportObj> userReportObjList = getGroupedUserReportList(gameSoftTransactionPage);


        UserDetailsResponse userDetailsResponse = UserDetailsResponse.builder()
                .status(true)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .userReportObjList(userReportObjList)
                .build();

        return ResponseEntity.ok(userDetailsResponse);
    }

    private List<UserReportObj> getGroupedUserReportList(List<GameSoftTransaction> gameSoftTransactionPage) {
        return gameSoftTransactionPage.stream().collect(Collectors.groupingBy(
                        g -> new GameTransactionGroupKey(
                                g.getGameSoftTransitionUser().getAr7Id(),
                                g.getGameType().getId()
                        ),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    int totalBetAmount = (int) list.stream().mapToDouble(GameSoftTransaction::getBetAmount).sum();
                                    int sumTotalWinAmount = (int) list.stream().mapToDouble(GameSoftTransaction::getAmount).sum();

                                    int winLoseAmount = sumTotalWinAmount-totalBetAmount;
                                    int totalWinAmount = winLoseAmount+totalBetAmount;
                                    log.info("List Current i , {}", list.get(0));
                                    return UserReportObj.builder()
                                            .userId(list.get(0).getGameSoftTransitionUser().getAr7Id())
                                            .gameTypeName(list.get(0).getGameType().getDescription())
                                            .totalBetCount(list.size())
                                            .gameTypeId(list.get(0).getGameType().getId())
                                            .totalBetAmount(totalBetAmount)
                                            .totalWinAmount(totalWinAmount)
                                            .winLoseAmount(winLoseAmount)
                                            .build();
                                }
                        )
                ))
                .values()  // Extract the grouped values
                .stream()
                .filter(Objects::nonNull)  // Remove null entries
                .toList();
    }

    @Override
    public ResponseEntity<UserDetailsResponse> getUserDetailReport(String token, UserPlayDetailTransitionGroupKey request,Pageable pageable) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        if(!ar7Id.startsWith("AY")){
            throw new UnauthorizedException("You are not admin");
        }

        Page<GameSoftTransaction> gameSoftTransactionPage = gameSoftTransactionRepo.findByCreatedOnBetweenAndGameSoftTransitionUser_Ar7IdAndGameType_IdAndStatus(DateUitls.parseDateTime(request.getStartDate()),DateUitls.parseDateTime(request.getEndDate()),request.getAr7Id(),request.getGameTypeId(),"SETTLED",pageable);
        log.info(Constraint.RETRIEVE_SUCCESS_MESSAGE);

        List<UserDetailObj> userDetailObjList = buildUserDetailObjList(gameSoftTransactionPage);
        return buildUserDetailResponse(userDetailObjList,gameSoftTransactionPage);

    }

    @Override
    public ResponseEntity<UserDetailsResponse> getCommissionConfirmReport(String token, String role, String startDateString, String endDateString, Pageable pageable) {
        LocalDateTime startDate = DateUitls.parseDateTime(startDateString);
        LocalDateTime endDate = DateUitls.parseDateTime(endDateString);
        String ar7Id = jwtService.extractUsername(token.substring(7));
        if(!ar7Id.startsWith("AY")){
            throw new UnauthorizedException("You are not admin");
        }
        List<CommissionConfirm> commissionConfirmList = commissionConfirmRepo.findByConfirmDateBetween(startDate,endDate,pageable);
        List<CommissionConfirmReportObj> commissionConfirmReportObjList;
        if (role.equals("AGENT")){
           commissionConfirmReportObjList = commissionConfirmList.stream().map(obj ->
                    CommissionConfirmReportObj.builder()
                            .agentAr7Id(obj.getAgentAr7Id())
                            .agentWinLoseAmount(obj.getAgentWinLoseAmount())
                            .confirmDate(obj.getConfirmDate())
                            .build()).toList();
        } else if (role.equals("MASTER")) {
            commissionConfirmReportObjList = commissionConfirmList.stream().map(obj ->
                    CommissionConfirmReportObj.builder()
                            .masterAr7Id(obj.getMasterAr7Id())
                            .masterWinLoseAmount(obj.getMasterWinLoseAmount())
                            .confirmDate(obj.getConfirmDate())
                            .build()).toList();
        }else if (role.equals("SENIOR_MASTER")){
            commissionConfirmReportObjList = commissionConfirmList.stream().map(obj ->
                    CommissionConfirmReportObj.builder()
                            .seMasterA7Id(obj.getSeMasterA7Id())
                            .seMasterWinLoseAmount(obj.getSeMasterWinLoseAmount())
                            .confirmDate(obj.getConfirmDate())
                            .build()).toList();
        }else {
            throw new UnauthorizedException("Role must be one of these : 'AGENT','MASTER','SENIOR_MASTER'");
        }


        UserDetailsResponse userDetailsResponse = UserDetailsResponse.builder()
                .commissionConfirmReportObjList(commissionConfirmReportObjList)
                .currentPage(pageable.getPageNumber())
                .totalElements(commissionConfirmReportObjList.size())
                .totalPages(commissionConfirmReportObjList.size()/ pageable.getPageSize())
                .status(true)
                .statusCode(200)
                .build();
        return ResponseEntity.ok(userDetailsResponse);
    }

    private ResponseEntity<UserDetailsResponse> buildUserDetailResponse(List<UserDetailObj> userDetailObjList, Page<GameSoftTransaction> results) {
        var response = UserDetailsResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .userDetailReportList(userDetailObjList)
                .totalPages(results.getTotalPages())
                .totalElements(results.getTotalElements())
                .currentPage(results.getNumber())
                .pageSize(results.getSize())
                .build();
        return ResponseEntity.ok(response);
    }

    private List<UserDetailObj> buildUserDetailObjList(Page<GameSoftTransaction> transactionListOfUser) {
        
        return transactionListOfUser.stream().map(e->
             UserDetailObj.builder()
                    .betTime(e.getCreatedOn().toString())
                    .ar7Id(e.getGameSoftTransitionUser().getAr7Id())
                    .resultTime(e.getModifiedOn().toString())
                    .transactionId(e.getTransactionId())
                    .wagerId(e.getGameSoftWager().getId())
                    .gameCode(e.getGameCode())
                    .beforeBetAmount(e.getBetAmount())
                    .betAmount(e.getBetAmount())
                    .winAmount(e.getAmount()>=0?e.getAmount():0.0)
                    .transactionAmount(e.getAmount())
                    .status(e.getStatus())
                    .gameName(e.getGameType().getDescription())
                    .gameTypeCode(e.getGameType().getCode())
                    .build()).toList();

    }

    private String  checkStatus(Integer status) {
        if (status==100){
            return GameStatus.PENDING.name();
        } else if (status==101) {
            return GameStatus.SETTLE.name();
        }else {
            return GameStatus.VOID.name();
        }
    }


}
