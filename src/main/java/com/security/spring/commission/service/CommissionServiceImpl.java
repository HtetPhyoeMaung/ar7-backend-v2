package com.security.spring.commission.service;

import com.security.spring.commission.dto.*;
import com.security.spring.commission.entity.*;
import com.security.spring.commission.repo.*;
import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.gamesoft.gameType.dto.GameTypeObj;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameType.service.GameTypeService;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.transaction.repsitory.GameSoftTransactionRepo;
import com.security.spring.report.dto.GameStatus;
import com.security.spring.report.dto.UserDetailObj;
import com.security.spring.report.entity.SpaceTechGameReport;
import com.security.spring.report.repo.SpaceTechGameReportRepo;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.unit.services.UnitService;
import com.security.spring.user.dto.UserResponseObj;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.RegisterStatusRepo;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.role.Role;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.Constraint;
import com.security.spring.utils.GroupKey;
import com.security.spring.utils.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommissionServiceImpl implements CommissionService {



    private final UserService userService;

    private final UserCommissionRepo userCommissionRepo;

    private final SpaceTechGameReportRepo spaceTechGameReportRepo;

    private final RegisterStatusRepo registerStatusRepo;

    private final UserRepository userRepository;

    private final UserWinLoseDetailRepo userWinLoseDetailRepo;

    private final GameSoftTransactionRepo gameSoftTransactionRepo;

    private final AgentCommissionUserRepo agentCommissionUserRepo;

    private final JWTService jwtService;

    private final ObjectMapper objectMapper;

    private final PrefixCommissionRepo prefixCommissionRepo;

    private final CommissionConfirmRepo commissionConfirmRepo;

    private final UnitService unitService;

    private final GameTypeService gameTypeService;

    @Override
    public CommissionResponse saveCommission(CommissionRequest data, String token) {
        User user = userAuthenticate(token);


        User checkUser = userService.findByAr7Id(data.getDownLineAr7Id());


        checkRoleAndDownLineUser(checkUser, user);

        UserCommission checkUserCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(checkUser.getAr7Id(), data.getGameTypeCode());

        if (checkUserCommission != null) {
            throw new CustomAlreadyExistException("this user's commission is already exist");
        }

        GameType gameType = gameTypeService.findByCode(data.getGameTypeCode());

        if (!user.getRole().equals(Role.ADMIN)) {
            UserCommission myCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(user.getAr7Id(), data.getGameTypeCode());

            if (myCommission == null)
                throw new DataNotFoundException("You have no commission for this game type " + data.getGameTypeCode());

            if (myCommission.getCommission() <= data.getCommission()) {
                throw new IndexOutOfBoundsException("Your commission must be greater than the down line's commission.");
            }
        }

        UserCommission userCommission = UserCommission.builder()
                .commission(data.getCommission())
                .gameType(gameType)
                .user(checkUser)
                .upLineAr7Id(checkUser.getParentUserId())
                .build();

        userCommission = userCommissionRepo.save(userCommission);

        var commissionBoj = commissionObjBuilder(userCommission, checkUser, gameType);
        return buildCommissionResponse(commissionBoj);
    }

    private CommissionObj commissionObjBuilder(UserCommission userCommission, User checkUser, GameType gameType) {
        return CommissionObj.builder()
                .id(userCommission.getId())
                .agCommission(userCommission.getCommission())
                .gameTypeObj(GameTypeObj.builder()
                        .code(gameType.getCode())
                        .description(gameType.getDescription())
                        .id(gameType.getId())
                        .build())
                .downLineUser(UserResponseObj.builder()
                        .ar7Id(checkUser.getAr7Id())
                        .parentUserId(checkUser.getParentUserId())
                        .userName(checkUser.getName())
                        .role(checkUser.getRole())
                        .build())
                .status(true)
                .build();
    }

    private void checkRoleAndDownLineUser(User checkUser, User user) {
        if (!checkUser.getParentUserId().equals(user.getAr7Id())) {
            throw new UnauthorizedException("this checkUser is not in user's downLineUserList");
        }

        if (!checkRole(user, checkUser)) {
            throw new UnauthorizedException("this checkUser is not your near downLine.");
        }
    }

    @Override
    public CommissionResponse updateCommission(CommissionRequest data, String token) {
        User user = userAuthenticate(token);

        User checkUser = userService.findByAr7Id(data.getDownLineAr7Id());

        checkRoleAndDownLineUser(checkUser, user);

        UserCommission checkUserCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(checkUser.getAr7Id(), data.getGameTypeCode());

        if (checkUserCommission == null) {
            throw new DataNotFoundException("user's commission is not exist");
        }

        GameType gameType = gameTypeService.findByCode(data.getGameTypeCode());
        if (!user.getRole().equals(Role.ADMIN)) {
            UserCommission myCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(user.getAr7Id(), data.getGameTypeCode());
            if (myCommission == null)
                throw new DataNotFoundException("You have no commission for this game type " + data.getGameTypeCode());
            if (myCommission.getCommission() <= data.getCommission()) {
                throw new IndexOutOfBoundsException("Your commission must be greater than the down line's commission.");
            }
        }

        checkUserCommission.setCommission(data.getCommission());
        checkUserCommission.setGameType(gameType);
        checkUserCommission.setUser(checkUser);


        checkUserCommission = userCommissionRepo.save(checkUserCommission);

        var commissionObj = commissionObjBuilder(checkUserCommission, checkUser, gameType);
        return buildCommissionResponse(commissionObj);
    }

    @Override
    public CommissionResponse checkCommission(String  gameTypeCode, String token) {
        User user = userAuthenticate(token);
        GameType gameType = gameTypeService.findByCode(gameTypeCode);
        UserCommission userCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(user.getAr7Id(), gameTypeCode);
        var commission0bj = commissionObjBuilder(userCommission, user, gameType);
        return buildCommissionResponse(commission0bj);
    }

    @Override
    public CommissionResponse getCommissionByAr7Id(String ar7Id, String token) {

        String adminId = jwtService.extractUsername(token.substring(7));
        if (checkRoleByAr7Id(adminId) == Role.USER) {
            throw new UnauthorizedException("Unauthorized Role : " + adminId);
        }
        User user = userService.findByAr7Id(ar7Id);

        List<UserCommission> userCommissionList =userCommissionRepo.findByUser_ar7Id(user.getAr7Id());

        return CommissionResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .gameTypeCommissionObjList(userCommissionList.stream().map(objectMapper::mapToCommissionObj).toList())
                .build();


    }

    @Override
    public CommissionResponse calculateCommission(String token) {

       adminUnthrozation(token);
        List<UserWinLoseDetailObj> userWinLoseDetailObjList = getUserDetailByCalculationStatusFalse();

        return CommissionResponse.builder()
                .status(true)
                .statusCode(200)
                .message("API Good Working")
                .build();
    }

    @Override
    @Transactional
    public CommissionResponse confirmOrCancelCommission(String token, String agentAr7Id) {
        adminUnthrozation(token);

        // Fetch without restricting to > 0
        PrefixCommission prefixCommission = prefixCommissionRepo.findByAgentAr7IdAndAgentWinLoseAmountGreaterThan(agentAr7Id,0);
        if (prefixCommission == null) {
            throw new DataNotFoundException("No commissions found for agent: " + agentAr7Id);
        }

        // Proceed with negative or positive commissions
        UserUnits agentUnits = unitService.findByUser_Ar7Id(agentAr7Id);
        UserUnits masterUnits = unitService.findByUser_Ar7Id(prefixCommission.getMasterAr7Id());
        UserUnits seMasterUnits = unitService.findByUser_Ar7Id(prefixCommission.getSeMasterAr7Id());

        double beforeAgentUnit = agentUnits.getMainUnit();
        double beforeMasterUnit = masterUnits.getMainUnit();
        double beforeSeMasterUnit = seMasterUnits.getMainUnit();

        // Update balances (handles negatives)
        agentUnits.setMainUnit(beforeAgentUnit + prefixCommission.getAgentWinLoseAmount());
        masterUnits.setMainUnit(beforeMasterUnit + prefixCommission.getMasterWinLoseAmount());
        seMasterUnits.setMainUnit(beforeSeMasterUnit + prefixCommission.getSeMasterWinLoseAmount());

        List<UserUnits> userUnitsList = List.of(agentUnits, masterUnits, seMasterUnits);
        unitService.saveAll(userUnitsList);

        // Save confirmation
        CommissionConfirm commissionConfirm = CommissionConfirm.builder()
                .agentAr7Id(agentAr7Id)
                .masterAr7Id(prefixCommission.getMasterAr7Id())
                .seMasterA7Id(prefixCommission.getSeMasterAr7Id())
                .agentWinLoseAmount(prefixCommission.getAgentWinLoseAmount())
                .masterWinLoseAmount(prefixCommission.getMasterWinLoseAmount())
                .seMasterWinLoseAmount(prefixCommission.getSeMasterWinLoseAmount())
                .confirmDate(LocalDateTime.now())
                .build();
        commissionConfirm = commissionConfirmRepo.save(commissionConfirm);

        // Reset amounts
        prefixCommission.setAgentWinLoseAmount(0);
        prefixCommission.setMasterWinLoseAmount(0);
        prefixCommission.setSeMasterWinLoseAmount(0);
        prefixCommission.setComfirmDate(LocalDateTime.now());
        prefixCommissionRepo.save(prefixCommission);

        // Mark transactions as processed
        List<UserWinLoseDetail> userWinLoseDetailList = userWinLoseDetailRepo.findByParentAgentId(agentAr7Id);
        userWinLoseDetailList.forEach(obj -> {
            obj.setConfirm(true);
        });
        userWinLoseDetailRepo.saveAll(userWinLoseDetailList);

        // Build response object
        CommissionConfirmObj commissionConfirmObj = CommissionConfirmObj.builder()
                .agentAr7Id(agentAr7Id)
                .masterAr7Id(prefixCommission.getMasterAr7Id())
                .seMasterA7Id(prefixCommission.getSeMasterAr7Id())
                .beforeAgentBalance(beforeAgentUnit)
                .beforeMasterBalance(beforeMasterUnit)
                .beforeSeMasterBalance(beforeSeMasterUnit)
                .afterAgentBalance(agentUnits.getMainUnit())
                .afterMasterBalance(masterUnits.getMainUnit())
                .afterSeMasterBalance(seMasterUnits.getMainUnit())
                .agentWinLoseAmount(commissionConfirm.getAgentWinLoseAmount())
                .masterWinLoseAmount(commissionConfirm.getMasterWinLoseAmount())
                .seMasterWinLoseAmount(commissionConfirm.getSeMasterWinLoseAmount())
                .confirmDate(commissionConfirm.getConfirmDate())
                .build();

        return CommissionResponse.builder()
                .message("Commission confirmed successfully!")
                .status(true)
                .commissionConfirmObj(commissionConfirmObj)
                .statusCode(200)
                .build();
    }

    @Override
    public CommissionResponse calculatedCommissionListForCompleteDownline(String token, Pageable pageable) {
        adminUnthrozation(token);

        Page<UserWinLoseDetail> userWinLoseDetailPage = userWinLoseDetailRepo.findByAgentCommissionStatusAndConfirmIsFalse(true, pageable);
        if (userWinLoseDetailPage.isEmpty()) {
            throw new DataNotFoundException("There is no calculated commission.");
        }

        List<UserWinLoseDetail> userWinLoseDetailList = userWinLoseDetailPage.getContent();
        List<PrefixCommission> agentCommissionPrefixList = new ArrayList<>();

        List<AgentCommissionResponse> agentCommissionResponseList = userWinLoseDetailList.stream()
                .collect(Collectors.groupingBy(
                        UserWinLoseDetail::getParentAgentId, // Group by agent only
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    String agentId = list.get(0).getParentAgentId();
                                    User agent = userService.findByAr7Id(agentId);
                                    if (agent == null) return null;
                                    String masterId = agent.getParentUserId();
                                    User master = userService.findByAr7Id(masterId);
                                    if (master == null) return null;
                                    String seMasterId = master.getParentUserId();
                                    User seMaster = userService.findByAr7Id(seMasterId);
                                    if (seMaster == null) return null;

                                    double totalBetAmount = list.stream().mapToDouble(UserWinLoseDetail::getTotalBetAmount).sum();
                                    double totalWinAmount = list.stream().mapToDouble(UserWinLoseDetail::getTotalWinAmount).sum();
                                    long totalUsers = list.stream().map(UserWinLoseDetail::getAr7Id).distinct().count();

                                    // Aggregate commissions across all game types
                                    double agentWinLose = 0, masterWinLose = 0, seMasterWinLose = 0;
                                    Map<String, List<UserWinLoseDetail>> byGameType = list.stream()
                                            .collect(Collectors.groupingBy(UserWinLoseDetail::getGameTypeCode));
                                    for (Map.Entry<String , List<UserWinLoseDetail>> entry : byGameType.entrySet()) {
                                        String  gameTypeCode = entry.getKey();
                                        List<UserWinLoseDetail> gameList = entry.getValue();
                                        double bet = gameList.stream().mapToDouble(UserWinLoseDetail::getTotalBetAmount).sum();
                                        double win = gameList.stream().mapToDouble(UserWinLoseDetail::getTotalWinAmount).sum();
                                        UserCommission agentCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(agentId, gameTypeCode);
                                        UserCommission masterCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(masterId, gameTypeCode);
                                        UserCommission seMasterCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(seMasterId, gameTypeCode);

                                        agentWinLose += (agentCommission != null ? (bet - win) / 100 * agentCommission.getCommission() : 0);
                                        masterWinLose += (masterCommission != null && agentCommission != null ? (bet - win) / 100 * (masterCommission.getCommission() - agentCommission.getCommission()) : 0);
                                        seMasterWinLose += (seMasterCommission != null && masterCommission != null ? (bet - win) / 100 * (seMasterCommission.getCommission() - masterCommission.getCommission()) : 0);
                                    }

                                    PrefixCommission existPrefixCommission = prefixCommissionRepo.findByAgentAr7IdAndMasterAr7IdAndSeMasterAr7Id(agentId, masterId, seMasterId);
                                    if (existPrefixCommission != null) {
                                        // Overwrite instead of adding
                                        existPrefixCommission.setAgentWinLoseAmount(agentWinLose);
                                        existPrefixCommission.setMasterWinLoseAmount(masterWinLose);
                                        existPrefixCommission.setSeMasterWinLoseAmount(seMasterWinLose);
                                        existPrefixCommission.setTotalBetAmount(totalBetAmount);
                                        existPrefixCommission.setTotalDownLine(totalUsers);
                                        prefixCommissionRepo.save(existPrefixCommission);
                                    } else {
                                        PrefixCommission newPrefix = PrefixCommission.builder()
                                                .agentAr7Id(agentId)
                                                .masterAr7Id(masterId)
                                                .seMasterAr7Id(seMasterId)
                                                .totalDownLine(totalUsers)
                                                .totalBetAmount(totalBetAmount)
                                                .agentWinLoseAmount(agentWinLose)
                                                .masterWinLoseAmount(masterWinLose)
                                                .seMasterWinLoseAmount(seMasterWinLose)
                                                .build();
                                        agentCommissionPrefixList.add(newPrefix);
                                    }

                                    return AgentCommissionResponse.builder()
                                            .agentAr7Id(agentId)
                                            .masterAr7Id(masterId)
                                            .seMasterAr7Id(seMasterId)
                                            .totalUser(totalUsers)
                                            .totalBetAmount(totalBetAmount)
                                            .totalWinAmount(totalWinAmount)
                                            .agentWinLoseAmount(agentWinLose)
                                            .masterWinLoseAmount(masterWinLose)
                                            .seMasterWinLoseAmount(seMasterWinLose)
                                            .build();
                                }
                        )
                ))
                .values()
                .stream()
                .filter(Objects::nonNull)
                .toList();

        prefixCommissionRepo.saveAll(agentCommissionPrefixList);

        return CommissionResponse.builder()
                .statusCode(200)
                .status(true)
                .message("API Good Working")
                .agentCommissionResponseList(agentCommissionResponseList)
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements((int) userWinLoseDetailPage.getTotalElements())
                .totalPages(userWinLoseDetailPage.getTotalPages())
                .build();
    }

    private void adminUnthrozation(String token) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        if (!ar7Id.startsWith("AY")){
            throw new UnauthorizedException("Unauthorized Role");
        }
    }



    public List<UserWinLoseDetailObj> getUserDetailByCalculationStatusFalse() {
        log.info("Reached State One");
        List<GameSoftTransaction> gameSoftTransactionList = gameSoftTransactionRepo.findByWagerStatusAndIsCommissionCalculate("SETTLED",false);
        if (gameSoftTransactionList.isEmpty()){
            throw new DataNotFoundException("There is no game transactions for calculate");
        }
        List<UserDetailObj> userDetailObjList = gameSoftTransactionList
                .stream()
                .map(
                        obj ->{
                            obj.setCommissionCalculate(true);
                            return  UserDetailObj
                                .builder()
                                .betTime(obj.getCreatedOn().toString())
                                .resultTime(obj.getModifiedOn().toString())
                                .afterAmount(obj.getGameSoftTransitionUser().getUserUnits().getMainUnit()+obj.getAmount())
                                .beforeBetAmount(obj.getGameSoftTransitionUser().getUserUnits().getMainUnit())
                                .ar7Id(obj.getGameSoftTransitionUser().getAr7Id())
                                .status(obj.getStatus())
                                .transactionAmount(obj.getAmount())
                                .gameName(obj.getGameType().getDescription())
                                .gameCode(obj.getGameCode())
                                .gameTypeCode(obj.getGameType().getCode())
                                .transactionId(obj.getTransactionId())
                                .winAmount(obj.getAmount())
                                .betAmount(obj.getBetAmount())
                                .build();}
                ).toList();



          List<UserWinLoseDetail> userWinLoseDetailList = userDetailObjList.stream()
                .collect(Collectors.groupingBy(
                        obj -> new GroupKey(obj.getAr7Id(), obj.getGameCode(), obj.getGameName() ,obj.getGameTypeCode()),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list ->{
                                    log.info("Reached One, {}",list.size());
                                    String agentId = userService.findByAr7Id(list.get(0).getAr7Id()).getParentUserId();
                                    log.info("agent {}",agentId);
                                    if(agentId != null){
                                        String masterId = userService.findByAr7Id(agentId).getParentUserId();
                                        log.info("master {}",masterId);
                                        String  gameTypeCode = list.get(0).getGameTypeCode();
                                        UserCommission agentCommission = userCommissionRepo.findByUser_ar7IdAndGameType_code(agentId,gameTypeCode);

                                        return UserWinLoseDetail.builder()
                                                .ar7Id(list.get(0).getAr7Id())
                                                .gameCode(list.get(0).getGameCode())
                                                .parentAgentId(agentId)
                                                .masterId(masterId)
                                                .gameTypeName(list.get(0).getGameName())
                                                .totalBetAmount(list.stream().mapToDouble(UserDetailObj::getBetAmount).sum())
                                                .totalWinAmount(list.stream().mapToDouble(UserDetailObj::getWinAmount).sum())
                                                .gameTypeCode(list.get(0).getGameTypeCode())
                                                .agentCommissionPercentage(agentCommission != null? agentCommission.getCommission() : 0)
                                                .agentWinLose(agentCommission != null? (list.stream().mapToDouble(UserDetailObj::getBetAmount).sum()-list.stream().mapToDouble(UserDetailObj::getWinAmount).sum())/100 * agentCommission.getCommission() : 0)
                                                .calculateDate(LocalDateTime.now())
                                                .transactionLines(list.size())
                                                .build();
                                    }

                                    return null;

                                }
                        )
                ))
                .values()
                .stream()
                .toList();
          userWinLoseDetailList.forEach(obj->obj.setAgentCommissionStatus(true));
          gameSoftTransactionRepo.saveAll(gameSoftTransactionList);
          return mapToUserWinLoseDetailObjList(userWinLoseDetailRepo.saveAll(userWinLoseDetailList));
    }

    private static List<UserWinLoseDetailObj> mapToUserWinLoseDetailObjList(List<UserWinLoseDetail> userWinLoseDetails) {
        return userWinLoseDetails.stream().map(obj->
                UserWinLoseDetailObj.builder()
                        .id(obj.getId())
                        .ar7Id(obj.getAr7Id())
                        .agentWinLose(obj.getAgentWinLose())
                        .agentCommissionPercentage(obj.getAgentCommissionPercentage())
                        .parentAgentId(obj.getParentAgentId())
                        .totalWinAmount(obj.getTotalWinAmount())
                        .totalBetAmount(obj.getTotalBetAmount())
                        .transactionLines(obj.getTransactionLines())
                        .gameCode(obj.getGameCode())
                        .gameTypeCode(obj.getGameTypeCode())
                        .gameTypeName(obj.getGameTypeName())
                        .calculateDate(obj.getCalculateDate())
                        .agentCommissionPercentage(obj.getAgentCommissionPercentage())
                        .build()).toList();
    }

    private UserWinLoseDetail buildUserWinLoseDetail(GameSoftTransaction gameSoftTransaction,String parentId, LocalDateTime startDate, LocalDateTime endDate) {

        return UserWinLoseDetail.builder()
                .ar7Id(gameSoftTransaction.getGameSoftTransitionUser().getAr7Id())
                .totalWinAmount(gameSoftTransaction.getAmount())
                .totalBetAmount(gameSoftTransaction.getBetAmount())
                .parentAgentId(parentId)
                .gameCode(gameSoftTransaction.getGameCode())
                .transactionLines(1)
                .build();
    }


    private Role checkRoleByAr7Id(String ar7Id) {
        if (ar7Id.startsWith("AY")) {
            return Role.ADMIN;
        } else if (ar7Id.startsWith("SE")) {
            return Role.SENIORMASTER;
        } else if (ar7Id.startsWith("MS")) {
            return Role.MASTER;
        } else if (ar7Id.startsWith("AG")) {
            return Role.AGENT;
        } else {
            return Role.USER;
        }
    }

    private User userAuthenticate(String token) {
        String authToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(authToken);
        return userService.findByAr7Id(ar7Id);
    }

    private boolean checkRole(User user, User checkUser) {
        if (user.getRole().equals(Role.ADMIN)) {
            return checkUser.getRole().equals(Role.SENIORMASTER);
        } else if (user.getRole().equals(Role.SENIORMASTER)) {
            return checkUser.getRole().equals(Role.MASTER);
        } else if (user.getRole().equals(Role.MASTER)) {
            return checkUser.getRole().equals(Role.AGENT);
        } else {
            return false;
        }
    }


    @Override
    public CommissionResponse getDownLineCommission(String token, Pageable pageable) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        Page<UserCommission> userCommissionPage = userCommissionRepo.findByUpLineAr7Id(ar7Id,pageable);
        List<DownLineCommissionObj> downLineCommissionObjList =userCommissionPage.stream().map(obj->downLineCommissionObjBuilder(obj,obj.getUser(),obj.getGameType())).toList();

        return buildCommissionResponse(downLineCommissionObjList,userCommissionPage);
    }

    @Override
    public CommissionResponse calculateSpaceTechGameCommission(String token) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        if (!ar7Id.startsWith("AY")){
            throw new UnauthorizedException("Unauthorized");
        }
        List<SpaceTechGameReport> spaceTechGameReport = spaceTechGameReportRepo.findAllByCalculateStatus(false);
return null;
    }

    @Override
    public CommissionResponse findById(int id) {
        UserCommission userCommission = userCommissionRepo.findById(id).orElseThrow(()->
                new DataNotFoundException("User Commission not found by ID : "+ id));
        return CommissionResponse.builder()
                .status(true)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .gameTypeCommissionObj(objectMapper.mapToCommissionObj(userCommission))
                .build();
    }

    private DownLineCommissionObj downLineCommissionObjBuilder(UserCommission obj, User user, GameType gameType) {
        return DownLineCommissionObj.builder()
                .id(obj.getId())
                .gameTypeId(gameType.getId())
                .gameTypeName(gameType.getDescription())
                .commissionPercentage(obj.getCommission())
                .ar7Id(obj.getUser().getAr7Id())
                .build();
    }



    private CommissionResponse buildCommissionResponse(CommissionObj commissionObj) {
        return CommissionResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .commissionObj(commissionObj)
                .build();

    }
    private CommissionResponse buildCommissionResponse(List<DownLineCommissionObj> downLineCommissionObjList, Page<UserCommission> userCommissionPage) {
        return CommissionResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .downLineCommissionObjList(downLineCommissionObjList)
                .totalPages(userCommissionPage.getTotalPages())
                .pageSize(userCommissionPage.getSize())
                .totalElements(userCommissionPage.getTotalElements())
                .currentPage(userCommissionPage.getNumber())
                .build();

    }

    private String checkStatus(Integer status) {
        if (status == 100) {
            return GameStatus.PENDING.name();
        } else if (status == 101) {
            return GameStatus.SETTLE.name();
        } else {
            return GameStatus.VOID.name();
        }
    }
}