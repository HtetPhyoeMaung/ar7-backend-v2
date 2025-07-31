package com.security.spring.withdraw.service;

import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import com.security.spring.bank.bankName.repo.BankNameAuthRepo;
import com.security.spring.bank.bankName.repo.BankNameRepo;
import com.security.spring.config.JWTService;
import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.notification.dto.NotificationResponse;
import com.security.spring.notification.entity.Notification;
import com.security.spring.notification.repository.NotificationRepo;
import com.security.spring.unit.entity.TransitionHistory;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.unit.repo.TransitionHistoryRepo;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.MapperUtil;
import com.security.spring.utils.UUIDGenerator;
import com.security.spring.withdraw.dto.WithdrawObj;
import com.security.spring.withdraw.dto.WithdrawRequest;
import com.security.spring.withdraw.dto.WithdrawResponse;
import com.security.spring.withdraw.dto.WithdrawStatusRequest;
import com.security.spring.withdraw.entity.TempoSaveWithdraw;
import com.security.spring.withdraw.entity.Withdraw;
import com.security.spring.withdraw.repository.TempoSaveWithdrawRepo;
import com.security.spring.withdraw.repository.WithdrawRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService{


    private final UserRepository userRepository;

    private final BankNameAuthRepo bankNameAuthRepo;

    private final BankNameRepo bankNameRepo;

    private final WithdrawRepo withdrawRepo;

    private final TempoSaveWithdrawRepo tempoSaveWithdrawRepo;

    private final TransitionHistoryRepo transitionHistoryRepo;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final JWTService jwtService;

    private final UserService userService;

    private final NotificationRepo notificationRepo;



    @Override
    @Transactional
    public WithdrawResponse saveWithdraw(WithdrawRequest data, String ar7Id) {
        User currentUser = userService.findByAr7Id(ar7Id);

        log.info("Reached State 2");

        BankName bankName = bankNameRepo.findById(Long.valueOf(data.getBankNameId())).orElseThrow(()->
                new DataNotFoundException("Hi you bank name is not match please try again"));

        log.info("Reached State 3");
        log.info("CurrentUserId : {}",currentUser.getAr7Id());
        log.info("ParentUserId : {}",currentUser.getParentUserId());
        User parentUser = userService.findByAr7Id(currentUser.getParentUserId());
        log.info("Reached State 4");
        BankNameAuth bankNameAuth = bankNameAuthRepo.findByOwnerUserAndBankNameAndBankNameStatusIsTrue(parentUser,bankName).orElseThrow(()->
                new DataNotFoundException("Hello User this bank name not provided to you , please contact with your agent"));

        UserUnits currentUserUnit = currentUser.getUserUnits();
        if(data.getWithdrawAmount() > currentUserUnit.getMainUnit()){
            throw new DataNotFoundException("Hay Guy Your not sufficient balance for withdraw");
        }
        currentUserUnit.setMainUnit(currentUserUnit.getMainUnit() - data.getWithdrawAmount());;
        currentUser.setUserUnits(currentUserUnit);
        userRepository.save(currentUser);

        LocalDateTime withdrawDate = LocalDateTime.now();
        String adminTransitionId = UUIDGenerator.generateUUID();

        var tempoWithdrawSave = TempoSaveWithdraw
                .builder()
                .tempoWithdrawUser(currentUser)
                .amount(data.getWithdrawAmount())
                .saveDate(withdrawDate)
                .build();

        var responseObj = tempoSaveWithdrawRepo.save(tempoWithdrawSave);

        var withdrawObj = Withdraw
                .builder()
                .amount(data.getWithdrawAmount())
                .withdrawStatus(DepositStatus.PENDING)
                .withdrawBankAcc(data.getWithdrawAccName())
                .withdrawBankAccNumber(data.getWithdrawAccount())
                .description("Withdraw")
                .withdrawUser(currentUser)
                .parentUser(parentUser)
                .bankName(bankName)
                .actionTime(withdrawDate)
                .adminTransitionNumber(adminTransitionId)
                .withdrawTransitionNumber(data.getWithdrawTransitionNumber())
                .build();

        withdrawRepo.save(withdrawObj);
        Notification notification = Notification.builder()
                .type(Notification.Type.WITHDRAW)
                .message("User : "+currentUser.getAr7Id()+"requests a withdraw request. Amount : "+withdrawObj.getAmount())
                .senderId(currentUser.getAr7Id())
                .receiverId(currentUser.getParentUserId())
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        NotificationResponse notificationResponse = MapperUtil.mapToNotificationResponse(notification);
        simpMessagingTemplate.convertAndSendToUser(notification.getReceiverId(),"/queue/notification",notificationResponse);
        notificationRepo.save(notification);
        return WithdrawResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("Your Withdraw Is Pending , Please wait approve for agent.")
                .build();
    }

    @Override
    @Transactional
    public WithdrawResponse getWithdrawOwn(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate, DepositStatus withdrawStatus) {

        User currentUser = userService.findByAr7Id(ar7Id);


        // Create Pageable object
        Pageable paging = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        Page<Withdraw> withdrawPage;
        if (startDate==null && endDate==null){
            if(withdrawStatus == null){
                withdrawPage = withdrawRepo.findByWithdrawUser(currentUser,paging);
            }else if(withdrawStatus.equals(DepositStatus.COMPLETE) || withdrawStatus.equals(DepositStatus.CANCEL)){
                withdrawPage = withdrawRepo.findByWithdrawUserAndWithdrawStatusIn(currentUser, List.of(DepositStatus.COMPLETE, DepositStatus.CANCEL), paging);
            }else{
                withdrawPage = withdrawRepo.findByWithdrawUserAndWithdrawStatus(currentUser,withdrawStatus,paging);
            }

        }else {

            if(withdrawStatus == null){
                withdrawPage = withdrawRepo.findByActionTimeBetweenAndWithdrawUser(startDate,endDate,currentUser,paging);
            }else if(withdrawStatus.equals(DepositStatus.COMPLETE) || withdrawStatus.equals(DepositStatus.CANCEL)){
                withdrawPage = withdrawRepo.findByActionTimeBetweenAndWithdrawUserAndWithdrawStatusIn(startDate, endDate, currentUser, List.of(DepositStatus.COMPLETE, DepositStatus.CANCEL), paging);
            }else{
                withdrawPage = withdrawRepo.findByActionTimeBetweenAndWithdrawUserAndWithdrawStatus(startDate,endDate,currentUser,withdrawStatus,paging);
            }
        }
        // Get paginated result


        // Convert Deposit to DepositObj using map function
        return buildWithdrawResponse(withdrawPage,"All Withdraw For Own Process");
    }

    private WithdrawResponse buildWithdrawResponse(Page<Withdraw> withdrawPage, String message) {
        List<WithdrawObj> withdrawObjList = new LinkedList<>();
        withdrawPage.map(obj -> withdrawObjList.add(WithdrawObj.builder()
                .id(obj.getId())
                .withdrawUserName(obj.getWithdrawUser().getName())
                .withdrawAr7Id(obj.getWithdrawUser().getAr7Id())
                .parentUserName(obj.getParentUser().getName())
                .parentAR7Id(obj.getParentUser().getAr7Id())
                .bankNameId(obj.getBankName().getId())
                .bankNameName(obj.getBankName().getBankName())
                .withdrawBankAcc(obj.getWithdrawBankAcc())
                .withdrawBankAccNumber(obj.getWithdrawBankAccNumber())
                .userTransitionId(obj.getWithdrawTransitionNumber())
                .amount(obj.getAmount())
                .actionTime(obj.getActionTime())
                .withdrawStatus(obj.getWithdrawStatus())
                .build()));
        return WithdrawResponse.builder()
                .withdrawObjList(withdrawObjList)
                .message(message)
                .status(true)
                .statusCode(200)
                .currentPage(withdrawPage.getNumber())
                .totalElements(withdrawPage.getTotalElements())
                .totalPages(withdrawPage.getTotalPages())
                .pageSize(withdrawPage.getSize())
                .build();
    }

    @Override
    public WithdrawResponse getWithdrawTo(String token, int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        User user = userService.findByAr7Id(ar7Id);

        // Create Pageable object
        Pageable paging = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        Page<Withdraw> withdrawPage;
        if (startDate==null && endDate==null){
            withdrawPage = withdrawRepo.findByParentUserAndWithdrawStatus(user,DepositStatus.PENDING,paging);
        }else {
            withdrawPage = withdrawRepo.findByActionTimeBetweenAndParentUserAndWithdrawStatus(startDate,endDate,user,DepositStatus.PENDING,paging);
        }
        // Convert Deposit to DepositObj using map function
        return buildWithdrawResponse(withdrawPage,"All Withdraw For To Process");
    }

    @Override
    public WithdrawResponse getWithdrawFrom(String ar7Id, int page, int size) {
        Optional<User> currentUser = userRepository.findByAr7Id(ar7Id);
        User currentUserGet = currentUser.get();


        // Create Pageable object
        Pageable paging = PageRequest.of(page, size);

        // Get paginated result
        Page<Withdraw> withdrawPage = withdrawRepo.findByWithdrawUserAndWithdrawStatus(currentUserGet,DepositStatus.PENDING,paging);

        // Convert Deposit to DepositObj using map function
        return buildWithdrawResponse(withdrawPage,"All Withdraw For From Process");
    }


    @Override
    @Transactional
    public WithdrawResponse getWithdrawById(Long widthDrawId,String ar7Id) {
//        Optional<User> requestUser = userRepository.findByAr7Id(ar7Id);
//        User userGet = requestUser.get();
//        if(userGet.getPartenUserId().equals(Role.ADMIN)){
//            Optional<Withdraw> withdrawObj = withdrawRepo.findById(widthDrawId);
//            List<WithdrawObj> withdrawObjRes = withdrawObj
//                    .stream()
//                    .map( obj -> WithdrawObj
//                            .builder()
//                            .id(obj.getId())
//                            .withdrawUserName(obj.getWithdrawUser().getName())
//                            .parentUserName(obj.getParentUser().getName())
//                            .amount(obj.getAmount())
//                            .actionTime(null)
//                            .withdrawStatus(obj.getWithdrawStatus())
//                            .build())
//                    .collect(Collectors.toList());
//            return WithdrawResponse
//                    .builder()
//                    .statusCode(HttpStatus.OK.value())
//                    .status(true)
//                    .message("API Work Success")
//                    .withdrawObjs( withdrawObjRes)
//                    .build();
//        }else{
//            Optional<Withdraw> withdrawObj = Optional.ofNullable(withdrawRepo.findByIdAndWithdrawUserOrParentUser(widthDrawId, userGet, userGet));
//            List<WithdrawObj> withdrawObjRes = withdrawObj
//                    .stream()
//                    .map( obj -> WithdrawObj
//                            .builder()
//                            .id(obj.getId())
//                            .withdrawUserName(obj.getWithdrawUser().getName())
//                            .parentUserName(obj.getParentUser().getName())
//                            .amount(obj.getAmount())
//                            .actionTime(null)
//                            .withdrawStatus(obj.getWithdrawStatus())
//                            .build())
//                    .collect(Collectors.toList());
//            return WithdrawResponse
//                    .builder()
//                    .statusCode(HttpStatus.OK.value())
//                    .status(true)
//                    .message("API Work Success")
//                    .withdrawObjs( withdrawObjRes)
//                    .build();
//        }
        return null;
    }

    @Override
    @Transactional
    public WithdrawResponse updateWithdraw(WithdrawStatusRequest data, String ar7Id) {

        User currentUser = userService.findByAr7Id(ar7Id);

        Optional<Withdraw> withdraw = Optional.ofNullable(withdrawRepo.findByIdAndParentUserAndWithdrawStatus(Long.valueOf(data.getWithdrawId()), currentUser, DepositStatus.PENDING));
        if(withdraw.isEmpty()){
            throw new DataNotFoundException("Can't find This withdraw");
        }
        Withdraw withdrawGet = withdraw.get();

        Optional<TempoSaveWithdraw> tempoSaveWithdraw = Optional.ofNullable(tempoSaveWithdrawRepo.findByTempoWithdrawUserAndAmountAndSaveDate(withdrawGet.getWithdrawUser(), withdrawGet.getAmount(),withdrawGet.getActionTime()));
        if(tempoSaveWithdraw.isEmpty()){
           throw new DataNotFoundException("Something Wrong Try Again.");
        }
        TempoSaveWithdraw tempoSaveWithdrawGet = tempoSaveWithdraw.get();
        UserUnits currentUnitObj = currentUser.getUserUnits();
        double currentUserBeforeAmt = currentUnitObj.getMainUnit();
        double withdrawUserBeforeAmt = withdrawGet.getWithdrawUser().getUserUnits().getMainUnit();
        if(data.getWithdrawStatus().equalsIgnoreCase("confirm")){
                currentUnitObj.setMainUnit(currentUnitObj.getMainUnit() + tempoSaveWithdrawGet.getAmount());
                currentUser.setUserUnits(currentUnitObj);
                userRepository.save(currentUser);



                withdrawGet.setWithdrawStatus(DepositStatus.COMPLETE);
                Withdraw returnWithdrawObj = withdrawRepo.save(withdraw.get());

                TransitionHistory saveTransitionObj = TransitionHistory
                        .builder()
                        .backendTransitionId(returnWithdrawObj.getAdminTransitionNumber())
                        .amount(returnWithdrawObj.getAmount())
                        .actionTime(returnWithdrawObj.getActionTime())
                        .description(returnWithdrawObj.getDescription())
                        .toUser(returnWithdrawObj.getWithdrawUser())
                        .fromUser(returnWithdrawObj.getParentUser())
                        .toUserBeforeBalance(withdrawUserBeforeAmt + returnWithdrawObj.getAmount())
                        .fromUserBeforeBalance(currentUserBeforeAmt)
                        .toUserAfterBalance(withdrawUserBeforeAmt)
                        .fromUserAfterBalance(currentUserBeforeAmt + returnWithdrawObj.getAmount())
                        .build();

                transitionHistoryRepo.save(saveTransitionObj);

            tempoSaveWithdrawRepo.delete(tempoSaveWithdrawGet);
            Notification notification = Notification.builder()
                    .senderId(currentUser.getAr7Id())
                    .receiverId(withdrawGet.getWithdrawUser().getAr7Id())
                    .type(Notification.Type.WITHDRAW)
                    .message("You has successfully withdraw. Amount : "+withdrawGet.getAmount())
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .build();

            NotificationResponse notificationResponse = MapperUtil.mapToNotificationResponse(notification);
            simpMessagingTemplate.convertAndSendToUser(notificationResponse.getReceiverId(),"/queue/notification",notificationResponse);
            notification = notificationRepo.save(notification);
            return WithdrawResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("Withdraw Successfully.")
                .withdrawObj(buildWithdrawObj(returnWithdrawObj))
                .build();

        }else if(data.getWithdrawStatus().equalsIgnoreCase("cancel")){
            tempoSaveWithdrawRepo.delete(tempoSaveWithdrawGet);
            withdraw.get().setWithdrawStatus(DepositStatus.CANCEL);
            withdrawRepo.save(withdraw.get());
            double withdrawUserAfterAmt = withdrawUserBeforeAmt+withdrawGet.getAmount();
            User withdrawUser = withdrawGet.getWithdrawUser();
            withdrawUser.getUserUnits().setMainUnit(withdrawUserAfterAmt);
            userRepository.save(withdrawUser);
            Notification notification = Notification.builder()
                    .senderId(currentUser.getAr7Id())
                    .receiverId(withdrawGet.getWithdrawUser().getAr7Id())
                    .type(Notification.Type.WITHDRAW)
                    .message("You has rejected withdraw. Amount : "+withdrawGet.getAmount())
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .build();

            NotificationResponse notificationResponse = MapperUtil.mapToNotificationResponse(notification);
            simpMessagingTemplate.convertAndSendToUser(notificationResponse.getReceiverId(),"/queue/notification",notificationResponse);
            notificationRepo.save(notification);
            return WithdrawResponse
                    .builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(true)
                    .message("Withdraw Cancel.")
                    .build();
        }else{
            throw new DataNotFoundException("Withdraw Status is something wrong");
        }
    }

    @Override
    public WithdrawResponse getDepositAll(String token, int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        User user = userService.findByAr7Id(ar7Id);

        // Create Pageable object
        Pageable paging = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        Page<Withdraw> withdrawPage;
        if (startDate==null && endDate==null){
            withdrawPage = withdrawRepo.findByParentUserAndWithdrawStatus(user,DepositStatus.COMPLETE,paging);
        }else {
            withdrawPage = withdrawRepo.findByActionTimeBetweenAndParentUserAndWithdrawStatus(startDate,endDate,user,DepositStatus.COMPLETE,paging);
        }
        // Get paginated result
        // Convert Deposit to DepositObj using map function
        return buildWithdrawResponse(withdrawPage,"All Down-Line Withdraw List");
    }

    private WithdrawObj buildWithdrawObj(Withdraw returnWithdrawObj) {
        return WithdrawObj.builder()
                .withdrawAr7Id(returnWithdrawObj.getWithdrawUser().getAr7Id())
                .withdrawStatus(returnWithdrawObj.getWithdrawStatus())
                .withdrawBankAcc(returnWithdrawObj.getWithdrawBankAcc())
                .withdrawUserName(returnWithdrawObj.getWithdrawUser().getName())
                .parentAR7Id(returnWithdrawObj.getParentUser().getAr7Id())
                .actionTime(returnWithdrawObj.getActionTime())
                .amount(returnWithdrawObj.getAmount())
                .id(returnWithdrawObj.getId())
                .userTransitionId(returnWithdrawObj.getWithdrawTransitionNumber())
                .withdrawStatus(returnWithdrawObj.getWithdrawStatus())
                .build();

    }
}
