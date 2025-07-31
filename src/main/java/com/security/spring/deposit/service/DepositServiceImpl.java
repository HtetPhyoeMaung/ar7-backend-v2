package com.security.spring.deposit.service;

import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankAcc.repo.BankAccRepo;
import com.security.spring.bank.bankName.repo.BankNameRepo;
import com.security.spring.config.JWTService;
import com.security.spring.deposit.dto.DepositObj;
import com.security.spring.deposit.dto.DepositRequest;
import com.security.spring.deposit.dto.DepositResponse;
import com.security.spring.deposit.entity.Deposit;
import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.deposit.repo.DepositRepo;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.notification.dto.NotificationResponse;
import com.security.spring.notification.entity.Notification;
import com.security.spring.notification.service.NotificationService;
import com.security.spring.promotion.entity.PromotionUnit;
import com.security.spring.promotion.entity.TicketBox;
import com.security.spring.promotion.repository.PromotionUnitRepository;
import com.security.spring.promotion.service.TicketBoxService;
import com.security.spring.unit.entity.TransitionHistory;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.unit.repo.TransitionHistoryRepo;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.role.Role;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService{

    private static final Logger log = LoggerFactory.getLogger(DepositServiceImpl.class);
    private final UserRepository userRepository;

    private final BankAccRepo bankAccRepo;

    private final BankNameRepo bankNameRepo;

    private final DepositRepo depositRepo;

    private final TransitionHistoryRepo transitionHistoryRepo;

    private final UserService userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final NotificationService notificationService;

    private final JWTService jwtService;

    private final TicketBoxService ticketBoxService;

    private final PromotionUnitRepository promotionUnitRepository;

    @Override
    @Transactional
    public DepositResponse saveDeposit(DepositRequest data, String ar7Id) {
        User currentUser = userService.findByAr7Id(ar7Id);

        if(currentUser.getRole().equals(Role.ADMIN)){
            throw new DataNotFoundException("Hello Admin You are admin so can't use deposit service");
        }
        BankAcc parentUserBankAcc;
        User parentUser = userService.findByAr7Id(currentUser.getParentUserId());

            parentUserBankAcc = bankAccRepo.findByIdAndOwnerUser(data.getParentBankAccId(), parentUser).orElseThrow(()->
                    new DataNotFoundException("Hello User This bank Account are not your parent"));

        if(!parentUserBankAcc.isAccountStatus()){
            throw new DataNotFoundException("Hello User this bank account is not authorize from your parent to make deposit");
        }

        if(checkTransactionId(data.getUserTransitionId())){
            throw new CustomAlreadyExistException("user transaction is already exists.");
        };

        DepositStatus depositStatus = DepositStatus.PENDING;
        LocalDateTime depositDate = LocalDateTime.now();

        Deposit depositObj = Deposit
                .builder()
                .amount(data.getAmount())
                .fromAcc(currentUser)
                .toAcc(parentUser)
                .accountNumber(data.getAccountNumber())
                .accountName(data.getAccountName())
                .userTransitionId(data.getUserTransitionId())
                .remark(data.getDepositStatus())
                .status(depositStatus)
                .transferTime(depositDate)
                .userTransitionId(data.getUserTransitionId())
                .build();

        depositRepo.save(depositObj);
        Notification notification = Notification.builder()
                .senderId(currentUser.getAr7Id())
                .receiverId(currentUser.getParentUserId())
                .type(Notification.Type.DEPOSIT)
                .message("User : "+currentUser.getAr7Id()+"requests a deposit request. Amount : "+depositObj.getAmount())
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();
        notification = notificationService.save(notification);
        NotificationResponse notificationResponse = MapperUtil.mapToNotificationResponse(notification);
        log.info("Parent User Id : {}",currentUser.getParentUserId());
        simpMessagingTemplate.convertAndSendToUser(currentUser.getParentUserId(),"/queue/notification",notificationResponse);
        return DepositResponse
                .builder()
                .message("Your " +
                        " Is Pending Please Wait A Few Minutes")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    private boolean checkTransactionId(String userTransitionId) {
        if(userTransitionId.length() != 6){
            throw new UnauthorizedException("user transaction id should be 6 digit");
        }else{
            return depositRepo.existsByUserTransitionId(userTransitionId);
        }
    }

    @Override
    @Transactional
    public DepositResponse getDepositForMe(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate, DepositStatus depositStatus) {
        User currentUser = userRepository.findByAr7Id(ar7Id).orElseThrow(()->
                new DataNotFoundException("User Not Found By "+ar7Id));

        // Create Pageable object
        Pageable paging = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        Page<Deposit> depositPage;
        if (startDate==null && endDate==null){
            if(depositStatus == null){
                depositPage = depositRepo.findByFromAcc(currentUser,paging);
            }else if (depositStatus.equals(DepositStatus.COMPLETE) || depositStatus.equals(DepositStatus.CANCEL)){
                depositPage = depositRepo.findByFromAccAndStatusIn(currentUser, List.of(DepositStatus.COMPLETE, DepositStatus.CANCEL), paging);
            }else{
                depositPage = depositRepo.findByFromAccAndStatus(currentUser,depositStatus,paging);
            }


        }else {

            if(depositStatus == null){
                depositPage = depositRepo.findByTransferTimeBetweenAndFromAcc(startDate,endDate,currentUser,paging);
            }else if(depositStatus.equals(DepositStatus.COMPLETE) || depositStatus.equals(DepositStatus.CANCEL)){
                depositPage = depositRepo.findByTransferTimeBetweenAndFromAccAndStatusIn(startDate, endDate, currentUser, List.of(DepositStatus.COMPLETE, DepositStatus.CANCEL), paging);
            }else{
                depositPage = depositRepo.findByTransferTimeBetweenAndFromAccAndStatus(startDate,endDate,currentUser,depositStatus,paging);
            }
        }

        // Convert Deposit to DepositObj using map function
        return buildDepositResponse(depositPage,"All Deposit History For You");


    }

    private DepositResponse buildDepositResponse(Page<Deposit> depositPage, String message) {
        List<DepositObj> depositObjList = new LinkedList<>();

        depositPage.map(obj -> depositObjList.add(DepositObj.builder()
                .id(obj.getId())
                .amount(obj.getAmount())
                .fromUsername(obj.getFromAcc().getName())
                .fromBankName(obj.getAccountName())
                .toUserName(obj.getToAcc().getName())
                .bankAcc(obj.getAccountNumber())
                .userTransitionId(obj.getUserTransitionId())
                .status(obj.getStatus())
                .actionTime(obj.getTransferTime())
                .build()));
        return DepositResponse.builder()
                .depositObjList(depositObjList)
                .status(true)
                .statusCode(200)
                .message(message)
                .totalPages(depositPage.getTotalPages())
                .totalElements(depositPage.getTotalElements())
                .currentPage(depositPage.getNumber())
                .pageSize(depositPage.getSize())
                .build();
    }

    @Override
    @Transactional
    public DepositResponse getDepositTo(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
       User currentUser = userRepository.findByAr7Id(ar7Id).orElseThrow(()->new DataNotFoundException("User Not Found By "+ar7Id));

        // Create Pageable object
        Pageable paging = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        Page<Deposit> depositPage;
        // Get paginated result
        if (startDate==null){
            depositPage = depositRepo.getDepositByToAccAndStatus(currentUser,DepositStatus.PENDING,paging);
        }else {
            depositPage  = depositRepo.getDepositByTransferTimeBetweenAndToAccAndStatus(
                    startDate,
                    endDate,
                    currentUser,
                    DepositStatus.PENDING,
                    paging
            );
        }


        // Convert Deposit to DepositObj using map function
        return buildDepositResponse(depositPage,"All Deposit History For 'To' Process");
    }

    @Override
    @Transactional
    public DepositResponse getDepositFrom(String ar7Id, int page, int size) {
        Optional<User> currentUser = userRepository.findByAr7Id(ar7Id);

        // Create Pageable object
        Pageable paging = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");

        // Get paginated result
        Page<Deposit> depositPage = depositRepo.getDepositByFromAccAndStatus(
                currentUser.get(),
                DepositStatus.PENDING,
                paging
        );

        // Convert Deposit to DepositObj using map function
        return buildDepositResponse(depositPage,"All Deposit History For 'From' Process");
    }


    @Override
    @Transactional
    public DepositResponse getDepositAll(String token, int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        User user = userService.findByAr7Id(ar7Id);

        // Create Pageable object
        Pageable paging = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        Page<Deposit> depositPage;
        if (startDate==null && endDate==null){
            depositPage = depositRepo.findByToAccAndStatus(user,DepositStatus.COMPLETE,paging);
        }else {
            depositPage = depositRepo.findByActionTimeBetweenAndToAcc(startDate,endDate,user,paging);
        }
        // Get paginated result
        // Convert Deposit to DepositObj using map function
        return buildDepositResponse(depositPage,"All Deposit History.");
    }

    @Override
    @Transactional
    public DepositResponse updateDeposit(String ar7Id,DepositRequest data) {

        User currentUser = userService.findByAr7Id(ar7Id);

        String depositStatus = data.getDepositStatus().toLowerCase();

        Optional<Deposit> deposit = depositRepo.findById(data.getDepositId());
        if(deposit.isEmpty()){
            throw new DataNotFoundException("Deposit Object was not found");
        }
        Deposit depositGet = deposit.get();
        if(!depositGet.getStatus().equals(DepositStatus.PENDING)){
            throw new DataNotFoundException("Your Deposit Object already status changed");
        }
        if(!depositGet.getToAcc().getAr7Id().equals(currentUser.getAr7Id())){
            throw new DataNotFoundException("Hello User This Deposit is not for you");
        }
        User depositUser = depositGet.getFromAcc();
        UserUnits depositUserUnit = depositUser.getUserUnits();
        UserUnits currentUserUnit = currentUser.getUserUnits();
        String message;
        if(depositStatus.equals("confirm")){

            double depositUserBeforeAmt =depositUserUnit.getMainUnit();
            double currentUserBeforeAmt =currentUserUnit.getMainUnit();

            if(depositGet.getAmount() > currentUserUnit.getMainUnit()){
                throw new DataNotFoundException("Insufficient Balance");
            }

            currentUserUnit.setMainUnit(currentUserUnit.getMainUnit() - depositGet.getAmount());
            currentUser.setUserUnits(currentUserUnit);
            userRepository.save(currentUser);

            depositUserUnit.setMainUnit(depositUserUnit.getMainUnit() + depositGet.getAmount());
            depositUser.setUserUnits(depositUserUnit);
            userRepository.save(depositUser);
            depositGet.setStatus(DepositStatus.COMPLETE);

            Deposit returnDepositObj = depositRepo.save(depositGet);

            var transitionHistoryObj = TransitionHistory
                    .builder()
                    .backendTransitionId(returnDepositObj.getUserTransitionId())
                    .amount(returnDepositObj.getAmount())
                    .actionTime(returnDepositObj.getTransferTime())
                    .description(returnDepositObj.getRemark())
                    .fromUser(returnDepositObj.getFromAcc())
                    .toUser(returnDepositObj.getToAcc())
                    .fromUserBeforeBalance(depositUserBeforeAmt)
                    .toUserBeforeBalance(currentUserBeforeAmt)
                    .fromUserAfterBalance(depositUserBeforeAmt + returnDepositObj.getAmount())
                    .toUserAfterBalance(currentUserBeforeAmt - returnDepositObj.getAmount())
                    .build();

            transitionHistoryRepo.save(transitionHistoryObj);
            message = "You has success deposit";
        }else if(depositStatus.equals("cancel")){
            depositGet.setStatus(DepositStatus.CANCEL);
            Deposit returnDepositObj = depositRepo.save(depositGet);
            message = "You has canceled deposit";
        }else{
            throw new DataNotFoundException("Hello your deposit status is something wrong");
        }

        PromotionUnit promotionUnit = promotionUnitRepository.findById(1L)
                .orElse(null);

        if( promotionUnit != null && promotionUnit.getUnit() > 0){
            TicketBox ticketBox = ticketBoxService.createTicketBox(depositUser, depositGet.getAmount(), promotionUnit.getUnit());
        }

        Notification notification = Notification.builder()
                .message(message+depositGet.getAmount())
                .type(Notification.Type.DEPOSIT)
                .senderId(depositGet.getFromAcc().getParentUserId())
                .receiverId(depositGet.getFromAcc().getAr7Id())
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        notificationService.save(notification);
        NotificationResponse notificationResponse = MapperUtil.mapToNotificationResponse(notification);
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(depositGet.getFromAcc().getAr7Id()),"/queue/notification",notificationResponse);


        return DepositResponse
                .builder()
                .message("Deposit Process Success")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .build();
    }
}
