package com.security.spring.unit.services;

import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.notification.dto.NotificationResponse;
import com.security.spring.notification.entity.Notification;
import com.security.spring.notification.service.NotificationService;
import com.security.spring.rro.ResponseFormat;
import com.security.spring.unit.dto.*;
import com.security.spring.unit.entity.AdminUnitCreateHistory;
import com.security.spring.unit.entity.TransitionHistory;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.unit.repo.AdminUnitCreateHistoryRepo;
import com.security.spring.unit.repo.TransitionHistoryRepo;
import com.security.spring.unit.repo.UnitRepository;
import com.security.spring.user.entity.User;
import com.security.spring.user.dto.UserDAOImpl;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.ContextUtils;
import com.security.spring.utils.CurrentDateTime;
import com.security.spring.utils.MapperUtil;
import com.security.spring.utils.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.security.spring.user.role.Role.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService{

    private final UserDAOImpl userDAOImpl;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final AdminUnitCreateHistoryRepo adminUnitCreateHistoryRepo;
    private final TransitionHistoryRepo transitionHistoryRepo;
    private final JWTService jwtService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional
    public UnitResponse createUnit(String ar7Id, UnitRequest data) {

        User currentUser = userRepository.findByAr7Id(ar7Id)
                .orElseThrow(() -> new DataNotFoundException("User can't found by ar7Id : " + ar7Id));

        UserUnits currentUserUnitObj = currentUser.getUserUnits();
        currentUserUnitObj.setMainUnit(currentUserUnitObj.getMainUnit() + data.getMainUnit());
        unitRepository.save(currentUserUnitObj);

//        Create Unit History
        AdminUnitCreateHistory historyObj = AdminUnitCreateHistory.builder()
                .id(0)
                .unitAmount(data.getMainUnit())
                .dateTime(CurrentDateTime.currentDateTime())
                .build();

        adminUnitCreateHistoryRepo.save(historyObj);

        return UnitResponse
                .builder()
                .message("API Good Working")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @Override
    @Transactional
    public ResponseFormat changeProUnit(String ar7Id, double proUnit) {
//        ResponseFormat responseObj = null;
//        User returnUserObj = userServiceImpl.getUserObjByAr7Id(ar7Id);
//        UserUnits unitObj = new UserUnits();
//        User userObj = new User();
//        if(returnUserObj.getUserUnits().getMainUnit() > proUnit){
//            unitObj.setMainUnit(returnUserObj.getUserUnits().getMainUnit() - proUnit);
//            unitObj.setGameUnit(returnUserObj.getUserUnits().getGameUnit());
//            unitObj.setPromotionUnit(returnUserObj.getUserUnits().getPromotionUnit() + proUnit);
//            unitObj.setTickets(returnUserObj.getUserUnits().getTickets());
//            unitObj.setUnitId(returnUserObj.getUserUnits().getUnitId());
//
//            userObj.setUserId(returnUserObj.getUserId());
//            userObj.setName(returnUserObj.getName());
//            userObj.setEmail(returnUserObj.getEmail());
//            userObj.setAr7Id(returnUserObj.getAr7Id());
//            userObj.setPassword(returnUserObj.getPassword());
//            userObj.setRole(returnUserObj.getRole());
//            userObj.setLoginTime(returnUserObj.getLoginTime());
//            userObj.setSecretCode(returnUserObj.getSecretCode());
//            userObj.setStatus(returnUserObj.getStatus());
//            userObj.setUserUnits(unitObj);
//
//            User returnUserWithUnit = userDAOImpl.saveUser(userObj);
//            System.out.println("returnUserWithUnit");
//            System.out.println(returnUserWithUnit);
//            if (returnUserWithUnit != null) {
//                responseObj = ResponseFormat.builder()
//                        .status(true)
//                        .statusCode(200)
//                        .message("Complete Unit Transfer")
//                        .build();
//            }else{
//                responseObj = ResponseFormat.builder()
//                        .status(false)
//                        .statusCode(500)
//                        .message("Someting Wrong Change Again")
//                        .build();
//            }
//        }else{
//            responseObj = ResponseFormat.builder()
//                    .status(false)
//                    .statusCode(400)
//                    .message("Someting Wrong Create Aganin")
//                    .build();
//        }
//        return responseObj;
        return null;
    }

    @Override
    @Transactional
    public TransitionResponse unitTransferPlus(String ar7Id, TransitionRequest data) {
        User currentUser = userRepository.findByAr7Id(ar7Id)
                .orElseThrow(() -> new DataNotFoundException("User can't found by ar7Id : " + ar7Id));

        if(!Objects.equals(currentUser.getSecretCode(), data.getSecretCode())){
            throw new UnauthorizedException("Secret Code is Wrong! Please Try Again.");
        }

        UserUnits currentUserUnitObj = currentUser.getUserUnits();
        if(data.getAmount() > currentUserUnitObj.getMainUnit()){
            throw new DataNotFoundException("Insufficient Balance");
        }

        User toUser = userRepository.findByAr7Id(data.getToAr7UserId()).orElseThrow(
                () ->  new DataNotFoundException("Your User is not found")
        );


        if(currentUser.getRole().equals(ADMIN)){
            currentUserUnitObj.setMainUnit(currentUserUnitObj.getMainUnit() - data.getAmount());
            unitRepository.save(currentUserUnitObj);
            UserUnits toUserUnitObject = toUser.getUserUnits();
            toUserUnitObject.setMainUnit(toUserUnitObject.getMainUnit() + data.getAmount());
            unitRepository.save(toUserUnitObject);


            TransitionHistory transitionHistoryObj = TransitionHistory
                    .builder()
                    .backendTransitionId(UUIDGenerator.generateUUID())
                    .amount(data.getAmount())
                    .actionTime(CurrentDateTime.currentDateTime())
                    .description("Unit Plus")
                    .toUser(toUser)
                    .fromUser(currentUser)
                    .toUserBeforeBalance(toUserUnitObject.getMainUnit() - data.getAmount())
                    .fromUserBeforeBalance(currentUserUnitObj.getMainUnit() + data.getAmount())
                    .toUserAfterBalance(toUserUnitObject.getMainUnit())
                    .fromUserAfterBalance(currentUserUnitObj.getMainUnit())
                    .build();

            transitionHistoryRepo.save(transitionHistoryObj);

            return TransitionResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else if(currentUser.getRole().equals(USER) && toUser.getRole().equals(USER)){
            if(!currentUser.getParentUserId().equals(toUser.getParentUserId())){
                throw new DataNotFoundException("Hello user you and user are not same downline,so can't transfer unit");
            }
            currentUserUnitObj.setMainUnit(currentUserUnitObj.getMainUnit() - data.getAmount());
            unitRepository.save(currentUserUnitObj);
            UserUnits toUserUnitObject = toUser.getUserUnits();
            toUserUnitObject.setMainUnit(toUserUnitObject.getMainUnit() + data.getAmount());
            unitRepository.save(toUserUnitObject);

            TransitionHistory transitionHistoryObj = TransitionHistory
                    .builder()
                    .backendTransitionId(UUIDGenerator.generateUUID())
                    .amount(data.getAmount())
                    .actionTime(CurrentDateTime.currentDateTime())
                    .description("Unit Plus")
                    .toUser(toUser)
                    .fromUser(currentUser)
                    .toUserBeforeBalance(toUserUnitObject.getMainUnit() - data.getAmount())
                    .fromUserBeforeBalance(currentUserUnitObj.getMainUnit() + data.getAmount())
                    .toUserAfterBalance(toUserUnitObject.getMainUnit())
                    .fromUserAfterBalance(currentUserUnitObj.getMainUnit())
                    .build();
            transitionHistoryRepo.save(transitionHistoryObj);

            return TransitionResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            if (!currentUser.getAr7Id().equals(toUser.getParentUserId())) {
                throw new DataNotFoundException("Hello this user is not your downline,so can't transfer unit");
            }
            currentUserUnitObj.setMainUnit(currentUserUnitObj.getMainUnit() - data.getAmount());
            unitRepository.save(currentUserUnitObj);
            UserUnits toUserUnitObject = toUser.getUserUnits();
            toUserUnitObject.setMainUnit(toUserUnitObject.getMainUnit() + data.getAmount());
            unitRepository.save(toUserUnitObject);

            TransitionHistory transitionHistoryObj = TransitionHistory
                    .builder()
                    .backendTransitionId(UUIDGenerator.generateUUID())
                    .amount(data.getAmount())
                    .actionTime(CurrentDateTime.currentDateTime())
                    .description("Unit Plus")
                    .toUser(toUser)
                    .fromUser(currentUser)
                    .toUserBeforeBalance(toUserUnitObject.getMainUnit() - data.getAmount())
                    .fromUserBeforeBalance(currentUserUnitObj.getMainUnit() + data.getAmount())
                    .toUserAfterBalance(toUserUnitObject.getMainUnit())
                    .fromUserAfterBalance(currentUserUnitObj.getMainUnit())
                    .build();

            transitionHistoryRepo.save(transitionHistoryObj);

            return TransitionResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }

    @Override
    @Transactional
    public TransitionResponse unitTransferMinus(String ar7Id,TransitionRequest data) {
        User currentUser = userRepository.findByAr7Id(ar7Id)
                .orElseThrow(() -> new DataNotFoundException("User can't found by ar7Id : " + ar7Id));;
        UserUnits currentUserUnit = currentUser.getUserUnits();
        User toUser = userRepository.findByAr7Id(data.getToAr7UserId()).orElseThrow(() ->
                new DataNotFoundException("User Not Found"));

        UserUnits toUserUnitObj = toUser.getUserUnits();
        if(data.getAmount() > toUserUnitObj.getMainUnit()){
            throw new DataNotFoundException("Hello can't get amount from user selected, because of amount insufficient");
        }

        toUserUnitObj.setMainUnit(toUserUnitObj.getMainUnit() - data.getAmount());
        unitRepository.save(toUserUnitObj);

        currentUserUnit.setMainUnit(currentUserUnit.getMainUnit() + data.getAmount());
        unitRepository.save(currentUserUnit);

        TransitionHistory transitionHistoryObj = TransitionHistory
                .builder()
                .backendTransitionId(UUIDGenerator.generateUUID())
                .amount(data.getAmount())
                .actionTime(CurrentDateTime.currentDateTime())
                .description("Unit Minus")
                .toUser(toUser)
                .fromUser(currentUser)
                .toUserBeforeBalance(toUserUnitObj.getMainUnit() + data.getAmount())
                .fromUserBeforeBalance(currentUserUnit.getMainUnit() - data.getAmount())
                .toUserAfterBalance(toUserUnitObj.getMainUnit())
                .fromUserAfterBalance(currentUserUnit.getMainUnit())
                .build();

        transitionHistoryRepo.save(transitionHistoryObj);

        return TransitionResponse
            .builder()
            .statusCode(HttpStatus.OK.value())
            .status(true)
            .message("API Good Working")
            .build();
    }

    public TransitionResponse getAllCreateUnitHistory(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate){
        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.DESC,"dateTime");
        Page<AdminUnitCreateHistory> adminUnitCreateHistories = adminUnitCreateHistoryRepo.findByDateTimeBetween(startDate,endDate,pageable);
        List<AdminUnitCreateHistoryObj> adminUnitCreateHistoryObjList = adminUnitCreateHistories.stream()
                .map(obj->AdminUnitCreateHistoryObj.builder()
                        .createTime(obj.getDateTime())
                        .amount(obj.getUnitAmount())
                        .build()).toList();
        return buildAdminResponse(adminUnitCreateHistories,adminUnitCreateHistoryObjList,"All Create Unit Histories");
    }

    @Override
    @Transactional
    public TransitionResponse getAllTransitionHistory(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
        Page<TransitionHistory> transitionHistories = transitionHistoryRepo.findByActionTimeBetween(startDate,endDate,pageable);
        List<TransitionObj> transitionObjList = transitionHistories.stream().map(this::buildTransactionObj).toList();
         return buildTransactionResponse(transitionHistories,transitionObjList,"All Transaction History.");
    }

    private TransitionResponse buildTransactionResponse(Page<TransitionHistory> transitionHistories,
                                                  List<TransitionObj> transitionObjList, String message) {
        return TransitionResponse.builder()
                .currentPage(transitionHistories.getNumber())
                .transitionObjList(transitionObjList)
                .pageSize(transitionHistories.getSize())
                .totalPages(transitionHistories.getTotalPages())
                .totalElements(transitionHistories.getTotalElements())
                .statusCode(200)
                .message(message)
                .status(true)
                .currentPage(transitionHistories.getNumber())
                .build();
    }

    private TransitionResponse buildAdminResponse(Page<AdminUnitCreateHistory> adminUnitCreateHistories,
                                                  List<AdminUnitCreateHistoryObj> adminUnitCreateHistoryObjList, String message) {
        return TransitionResponse.builder()
                .currentPage(adminUnitCreateHistories.getNumber())
                .adminUnitCreateHistoryObjList(adminUnitCreateHistoryObjList)
                .pageSize(adminUnitCreateHistories.getSize())
                .totalPages(adminUnitCreateHistories.getTotalPages())
                .totalElements(adminUnitCreateHistories.getTotalElements())
                .statusCode(200)
                .message(message)
                .status(true)
                .currentPage(adminUnitCreateHistories.getNumber())
                .build();
    }

    @Override
    @Transactional
    public TransitionResponse getAllTransitionHistoryOwn(String token,int page, int size,LocalDateTime startDate, LocalDateTime endDate) {
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);

        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        User currentUser = userRepository.findByAr7Id(ar7Id).orElseThrow(
                () -> new DataNotFoundException("User Not Found")
        );
        Page<TransitionHistory> transitionHistories;
        if (startDate==null || endDate==null){
            transitionHistories = transitionHistoryRepo.findByFromUserOrToUser(currentUser,currentUser,pageable);
        }else {
            transitionHistories = transitionHistoryRepo.findByActionTimeBetweenAndFromUserOrToUser(startDate,
                    endDate,currentUser,currentUser,pageable);
        }
        List<TransitionObj> transitionObjList = transitionHistories
                .stream()
                .map(obj -> {
                    TransitionObj transitionObj =buildTransactionObj(obj, currentUser);
                    if(currentUser.getAr7Id().equals(obj.getFromUser().getAr7Id())) {
                         transitionObj.setTransitionStatus("Out");
                    } else {
                        transitionObj.setTransitionStatus("In");
                    }
                    return transitionObj;
                }).toList();

        return buildTransactionResponse(transitionHistories,transitionObjList,"All Transaction Histories For Own Process.");
    }

    @Override
    public TransitionResponse getAllTransitionHistoryByUserId(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userService.findByAr7Id(ar7Id);
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
       Page<TransitionHistory> transitionHistories = transitionHistoryRepo.findByActionTimeBetweenAndFromUserOrToUser(startDate,endDate,user,user,pageable);
        List<TransitionObj> transitionObjList = transitionHistories
                .stream()
                .map(obj -> {
                    TransitionObj transitionObj =buildTransactionObj(obj, user);
                    if(user.getAr7Id().equals(obj.getFromUser().getAr7Id())) {
                        transitionObj.setTransitionStatus("Out");
                    } else {
                        transitionObj.setTransitionStatus("In");
                    }
                    return transitionObj;
                }).toList();

        return buildTransactionResponse(transitionHistories,transitionObjList,"All Transaction Histories For "+ar7Id+".");
    }

    private TransitionObj buildTransactionObj(TransitionHistory obj) {
        return TransitionObj
                .builder()
                .id(obj.getId())
                .backendTransitionId(obj.getBackendTransitionId())
                .fromUserAr7Id(obj.getFromUser().getAr7Id())
                .toUserAr7Id(obj.getToUser().getAr7Id())
                .fromUserName(obj.getFromUser().getName())
                .toUserName(obj.getToUser().getName())
                .amount(obj.getAmount())
                .fromUserBeforeBalance(obj.getFromUserBeforeBalance())
                .fromUserAfterBalance(obj.getFromUserAfterBalance())
                .toUserBeforeBalance(obj.getToUserBeforeBalance())
                .toUserAfterBalance(obj.getToUserAfterBalance())
                .transitionTime(obj.getActionTime())
                .description(obj.getDescription())
                .build();
    }

    private TransitionObj buildTransactionObj(TransitionHistory obj,User currentUser) {
        return TransitionObj
                .builder()
                .id(obj.getId())
                .backendTransitionId(obj.getBackendTransitionId())
                .fromUserAr7Id(obj.getFromUser().getAr7Id())
                .toUserAr7Id(obj.getToUser().getAr7Id())
                .fromUserName(obj.getFromUser().getName())
                .toUserName(obj.getToUser().getName())
                .amount(obj.getAmount())
                .fromUserBeforeBalance(currentUser.getAr7Id().equals(obj.getFromUser().getAr7Id()) || currentUser.getRole() == ADMIN ? obj.getFromUserBeforeBalance() : null)
                .fromUserAfterBalance(currentUser.getAr7Id().equals(obj.getFromUser().getAr7Id()) || currentUser.getRole() == ADMIN  ? obj.getFromUserAfterBalance() : null)
                .toUserBeforeBalance(currentUser.getAr7Id().equals(obj.getToUser().getAr7Id()) || currentUser.getRole() == ADMIN ? obj.getToUserBeforeBalance() : null)
                .toUserAfterBalance(currentUser.getAr7Id().equals(obj.getToUser().getAr7Id()) || currentUser.getRole() == ADMIN ? obj.getToUserAfterBalance() : null)
                .transitionTime(obj.getActionTime())
                .description(obj.getDescription())
                .build();
    }


    @Override
    @Transactional
    public AccountCheckResponse accountCheck(String ar7Id) {
        String currentAr7Id = ContextUtils.getAr7IdFromContext();
        User user = userService.findByAr7Id(currentAr7Id);
        if(user.getAr7Id().equals(ar7Id)){
            throw new UnauthorizedException("you can't transfer unit to your self");
        }
        User currentSeniorMaster = null;
        if(!user.getRole().equals(ADMIN)){
            currentSeniorMaster = getSeniorMaster(user);
        }
        User checkingAccount = userRepository.findByAr7Id(ar7Id).orElseThrow(
                () -> new DataNotFoundException("Account Not Found Exception")
        );
        User toUserSeniorMaster = null;
        if(!user.getRole().equals(ADMIN)){
            toUserSeniorMaster = getSeniorMaster(checkingAccount);
        }

        if(user.getRole().equals(ADMIN) || checkingAccount.getAr7Id().equals(user.getParentUserId()) || user.getAr7Id().equals(checkingAccount.getParentUserId()) || isSameSeniorMaster(toUserSeniorMaster,currentSeniorMaster)){
            if(!checkingAccount.getStatus()){
                throw new DataNotFoundException("Account has Found but this account was temporary ban and contact with admin");
            }
            AccountCheckObj accountCheckObj = AccountCheckObj
                    .builder()
                    .accountName(checkingAccount.getName())
                    .ar7Id(checkingAccount.getAr7Id())
                    .build();

            return AccountCheckResponse
                    .builder()
                    .message("API Good Working")
                    .accountCheckObj(accountCheckObj)
                    .statusCode(HttpStatus.OK.value())
                    .status(true)
                    .build();
        }else{
            throw new UnauthorizedException("You has not permission,so you can't transfer this user by id : "+checkingAccount.getAr7Id());
        }




    }

    private boolean isSameSeniorMaster(User toUserSeniorMaster, User currentSeniorMaster) {
        if(toUserSeniorMaster == null){
            throw new UnauthorizedException("You can't directly transfer to admin.");
        }
        return toUserSeniorMaster.getAr7Id().equals(currentSeniorMaster.getAr7Id());
    }

    private User getSeniorMaster(User user) {
        if(user.getRole().equals(USER)){
            User agent = userService.findByAr7Id(user.getParentUserId());
            User master = userService.findByAr7Id(agent.getParentUserId());
            return userService.findByAr7Id(master.getParentUserId());
        }else if(user.getRole().equals(AGENT)){
            User master = userService.findByAr7Id(user.getParentUserId());
            return userService.findByAr7Id(master.getParentUserId());
        }else if(user.getRole().equals(MASTER)){
            return userService.findByAr7Id(user.getParentUserId());
        }else if (user.getRole().equals(SENIORMASTER)){
            return user;
        }else {
            throw new UnauthorizedException("Can't found senior master by ar7Id :"+user.getAr7Id());
        }
    }

    @Override
    @Transactional
    public UnitResponse getOwnUnit(String ar7Id) {
        User currentUser = userRepository.findByAr7Id(ar7Id).orElseThrow(
                () -> new DataNotFoundException("User Not Found")
        );

        UserUnits unitObj = currentUser.getUserUnits();

        UnitResponseObj unitResponseObj = UnitResponseObj
                .builder()
                .mainUnit(unitObj.getMainUnit())
                .gameUnit(unitObj.getGameUnit())
                .promotionUnit(unitObj.getPromotionUnit())
                .tickets(unitObj.getTickets())
                .build();
        return UnitResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("API Good Working")
                .unitResponseObj(unitResponseObj)
                .build();
    }

    @Override
    public UserUnits findByUser_Ar7Id(String ar7Id) {
        return unitRepository.findByUser_Ar7Id(ar7Id).orElseThrow(()->
                new DataNotFoundException("User Units Not Found By "+ar7Id));
    }

    @Override
    public void saveAll(List<UserUnits> userUnitsList) {
      unitRepository.saveAll(userUnitsList);
    }

    @Override
    public TransitionResponse fastTransferPlus(TransitionRequest data) {
        String currentAr7Id = ContextUtils.getAr7IdFromContext();
        User currentUser = userService.findByAr7Id(currentAr7Id);
        if(currentUser.getAr7Id().equals(data.getToAr7UserId())){
            throw new UnauthorizedException("you can't transfer unit to your self");
        }
        User currentSeniorMaster = null;
        if(!currentUser.getRole().equals(ADMIN)){
            currentSeniorMaster = getSeniorMaster(currentUser);
        }
        UserUnits currentUserUnitObj = currentUser.getUserUnits();

        User toUser = userService.findByAr7Id(data.getToAr7UserId());

        User toUserSeniorMaster = null;
        if(!toUser.getRole().equals(ADMIN)){
            toUserSeniorMaster = getSeniorMaster(toUser);
        }

        if(!Objects.equals(currentUser.getSecretCode(), data.getSecretCode())){
            throw new UnauthorizedException("Secret Code is Wrong! Please Try Again.");
        }

        if(data.getAmount() > currentUserUnitObj.getMainUnit()){
            throw new DataNotFoundException("Insufficient Balance");
        }


        if(currentUser.getRole().equals(ADMIN) || toUser.getAr7Id().equals(currentUser.getParentUserId()) || isSameSeniorMaster(toUserSeniorMaster,currentSeniorMaster)){
            currentUserUnitObj.setMainUnit(currentUserUnitObj.getMainUnit() - data.getAmount());
            unitRepository.save(currentUserUnitObj);
            UserUnits toUserUnitObject = toUser.getUserUnits();
            toUserUnitObject.setMainUnit(toUserUnitObject.getMainUnit() + data.getAmount());
            unitRepository.save(toUserUnitObject);



            TransitionHistory transitionHistoryObj = TransitionHistory
                    .builder()
                    .backendTransitionId(UUIDGenerator.generateUUID())
                    .amount(data.getAmount())
                    .actionTime(CurrentDateTime.currentDateTime())
                    .description("fast Unit Transfer Plus")
                    .toUser(toUser)
                    .fromUser(currentUser)
                    .toUserBeforeBalance(toUserUnitObject.getMainUnit() - data.getAmount())
                    .fromUserBeforeBalance(currentUserUnitObj.getMainUnit() + data.getAmount())
                    .toUserAfterBalance(toUserUnitObject.getMainUnit())
                    .fromUserAfterBalance(currentUserUnitObj.getMainUnit())
                    .build();
            transitionHistoryRepo.save(transitionHistoryObj);

            Notification notification = Notification.builder()
                    .senderId(currentUser.getAr7Id())
                    .receiverId(toUser.getAr7Id())
                    .type(Notification.Type.FAST_TRANSFER)
                    .message("User : "+currentUser.getAr7Id()+" transfer unit . Amount : "+data.getAmount())
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .build();
            notification = notificationService.save(notification);
            NotificationResponse notificationResponse = MapperUtil.mapToNotificationResponse(notification);
            simpMessagingTemplate.convertAndSendToUser(toUser.getAr7Id(),"/queue/notification",notificationResponse);

            return TransitionResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }else{
            throw new UnauthorizedException("You can't transfer this user id + "+data.getToAr7UserId());
        }

    }
}





