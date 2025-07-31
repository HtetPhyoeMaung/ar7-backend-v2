package com.security.spring.bank.bankType.service;

import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankAcc.repo.BankAccRepo;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import com.security.spring.bank.bankName.repo.BankNameAuthRepo;
import com.security.spring.bank.bankType.dto.*;
import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.bank.bankType.entity.BankTypeAuth;
import com.security.spring.bank.bankType.repo.BankTypeAuthRepo;
import com.security.spring.bank.bankType.repo.BankTypeRepo;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.role.Role;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.Constraint;
import com.security.spring.utils.ContextUtils;
import com.security.spring.utils.ObjectMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BankTypeServiceImpl implements BankTypeService{



    private final BankTypeRepo bankTypeRepo;



    private final BankTypeAuthRepo bankTypeAuthRepo;

    private final BankNameAuthRepo bankNameAuthRepo;

    private final UserRepository userRepository;

    private final UserService userService;

    private final BankAccRepo bankAccRepo;

    private final ObjectMapper objectMapper;


    @Override
    @Transactional
    public BankTypeResponse saveBankType(BankTypeRequest data) {
        if (existsByName(data.getBankTypeName())){
            throw new CustomAlreadyExistException("BankType is already exists!");
        }
        BankType saveObj = BankType
                .builder()
                .bankTypeName(data.getBankTypeName())
                .build();

       BankType responseObj = bankTypeRepo.save(saveObj);

        BankTypeObj bankTypeObj = objectMapper.mapToBankTypeObj(responseObj);


        return BankTypeResponse
                .builder()
                .bankTypeObj(bankTypeObj)
                .message("Create BankType Successfully")
                .statusCode(200)
                .status(true)
                .build();
    }

    private boolean existsByName(@NotEmpty(message = "Please Enter Bank Type Name") String bankTypeName) {
        return bankTypeRepo.existsByBankTypeName(bankTypeName);
    }

    @Override
    @Transactional
    public BankTypeResponse getBankTypeAll() {
        List<BankType> bankTypeList = bankTypeRepo.findAll();
        var bankTypeObjList = bankTypeList.stream().map(
                obj -> BankTypeObj
                        .builder()
                        .bankTypeId(obj.getId())
                        .bankTypeName(obj.getBankTypeName())
                        .build()
        ).collect(Collectors.toList());
        return BankTypeResponse
                .builder()
                .bankTypeObjList(bankTypeObjList)
                .message("Successful API")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @Override
    @Transactional
    public BankTypeResponse findBankTypeById(Long bankTypeId) {
        BankType bankType = findById(bankTypeId);

        BankTypeObj bankTypeObj = objectMapper.mapToBankTypeObj(bankType);
        return BankTypeResponse
                .builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("API Successfully")
                .bankTypeObj(bankTypeObj)
                .build();
    }

    private BankType findById(Long bankTypeId) {
        return  bankTypeRepo.findById(bankTypeId).orElseThrow(()->
                new DataNotFoundException("BankType not found by ID : "+bankTypeId));
    }

    @Override
    @Transactional
    public BankTypeResponse updateBankType(BankTypeRequest data) {
        BankType bankType = findById(data.getBankTypeId());
        bankType.setBankTypeName(data.getBankTypeName());
        bankTypeRepo.save(bankType);
        return BankTypeResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message(Constraint.UPDATED_SUCCESS_MESSAGE + bankType.getBankTypeName())
                .build();
    }

    @Override
    @Transactional
    public BankTypeAuthResponse saveBankTypeAuth(BankTypeAuthRequest data,String ar7Id) {

        User currentUser = userService.findByAr7Id(ar7Id);
        BankType bankType = findById(data.getBankTypeId());

        if(currentUser.getRole().equals(Role.ADMIN)){
            if (existsBankTypeAuth(currentUser,bankType)){
                throw new CustomAlreadyExistException("You're already authenticate this BankType.");
            }
            BankTypeAuth bankTypeAuth = BankTypeAuth.builder()
                    .bankType(bankType)
                    .ownerUser(currentUser)
                    .bankTypeStatus(true)
                    .initStatus(1)
                    .build();

            bankTypeAuth = bankTypeAuthRepo.save(bankTypeAuth);
            return BankTypeAuthResponse
                    .builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(true)
                    .message("Authentication Success" + bankTypeAuth.getBankType().getBankTypeName())
                    .bankTypeAuthObj(objectMapper.mapToBankTypeAuthObj(bankTypeAuth))
                    .build();
        }else{
            User parentUser = userRepository.findByAr7Id(currentUser.getParentUserId()).orElseThrow(()->
                    new DataNotFoundException("You haven't parent user yet, so contact with admin"));
            
            BankTypeAuth parentBankTypeAuth = bankTypeAuthRepo.findByOwnerUserAndBankType(parentUser, bankType).orElseThrow(()->
                    new DataNotFoundException("This bank type can't control because of your parent user have not permission"));
            
            if(!parentBankTypeAuth.isBankTypeStatus()){
                throw new DataNotFoundException("This bank type can't control because of your parent user not permission for downline user");
            }

           if (bankTypeAuthRepo.findByOwnerUserAndBankType(currentUser, bankType).isPresent()){
               throw new CustomAlreadyExistException("Hello User this bank type already authentication");
           }

            BankTypeAuth bankTypeAuth = BankTypeAuth
                    .builder()
                    .bankTypeStatus(true)
                    .ownerUser(currentUser)
                    .bankType(bankType)
                    .initStatus(1)
                    .build();

             bankTypeAuth = bankTypeAuthRepo.save(bankTypeAuth);
            return BankTypeAuthResponse
                    .builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(true)
                    .message("Add Success" + bankTypeAuth.getBankType().getBankTypeName())
                    .bankTypeAuthObj(objectMapper.mapToBankTypeAuthObj(bankTypeAuth))
                    .build();
        }
    }

    private boolean existsBankTypeAuth(User currentUser, BankType bankType) {
        return bankTypeAuthRepo.existsByOwnerUser_Ar7IdAndBankType_Id(currentUser.getAr7Id(),bankType.getId());
    }

    private BankTypeAuth findBankTypeAuth(User currentUser, BankType bankType) {
        return bankTypeAuthRepo.findByOwnerUserAndBankType(currentUser, bankType).orElseThrow(()->
                new DataNotFoundException("BankTypeAuth not found."));
    }

    @Override
    @Transactional
    public BankTypeAuthResponse getBankTypeAuthAll(String ar7Id) {
        User user = userService.findByAr7Id(ar7Id);
        if(user.getRole().equals(Role.ADMIN)){
            List<BankType> bankTypeList = bankTypeRepo.findAll();
            List<BankTypeAuth> bankTypeAuthList = bankTypeAuthRepo.findByOwnerUser(user);
            List<BankTypeAuthObj> bankTypeAuthObjList = new ArrayList<>();

            for(BankType bankType : bankTypeList) {
                BankTypeAuthObj bankTypeAuthObj = getBankTypeAuthObj(bankType, bankTypeAuthList);
                bankTypeAuthObjList.add(bankTypeAuthObj);
            }

            return BankTypeAuthResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .bankTypeAuthObjList(bankTypeAuthObjList)
                    .build();
        }else{
            User parentUser = userRepository.findByAr7Id(user.getParentUserId()).orElseThrow(()->
                    new DataNotFoundException("Hello User you haven't upline user yet, so please contact with admin"));

            List<BankTypeAuth> bankTypeAuthsFromParent = bankTypeAuthRepo.findByOwnerUserAndBankTypeStatusIsTrueAndInitStatus(parentUser,1);
            List<BankTypeAuth> bankTypeAuthFromCurrent = bankTypeAuthRepo.findByOwnerUser(user);
            log.info("BankTypeAuths From Parent : {}",bankTypeAuthsFromParent);
            log.info("BankTypeAuths From Current : {}",bankTypeAuthFromCurrent);
            List<BankTypeAuthObj> bankTypeAuthObjList = new ArrayList<>();

            for(BankTypeAuth parentBankTypeAuth : bankTypeAuthsFromParent) {
                BankTypeAuthObj bankTypeAuthObj = getBankTypeAuthObj(parentBankTypeAuth, bankTypeAuthFromCurrent);
                bankTypeAuthObjList.add(bankTypeAuthObj);
            }

            return BankTypeAuthResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .bankTypeAuthObjList(bankTypeAuthObjList)
                    .build();
        }
    }

    private static BankTypeAuthObj getBankTypeAuthObj(BankTypeAuth parentBankTypeAuth, List<BankTypeAuth> bankTypeAuthFromCurrent) {
        BankTypeAuthObj bankTypeAuthObj = new BankTypeAuthObj();
        boolean found = false;

        for(BankTypeAuth currentBankTypeAuth: bankTypeAuthFromCurrent){
            if(parentBankTypeAuth.getBankType().equals(currentBankTypeAuth.getBankType())){
                bankTypeAuthObj.setId(currentBankTypeAuth.getId());
                bankTypeAuthObj.setParentUserName(parentBankTypeAuth.getOwnerUser().getName());
                bankTypeAuthObj.setAvailableBankTypeId(currentBankTypeAuth.getBankType().getId());
                bankTypeAuthObj.setAvailableBankTypeName(currentBankTypeAuth.getBankType().getBankTypeName());
                bankTypeAuthObj.setBankTypeStatus(currentBankTypeAuth.isBankTypeStatus());
                bankTypeAuthObj.setInitialStatus(currentBankTypeAuth.getInitStatus());
                found = true;
                break;
            }

        }
        if(!found) {
            bankTypeAuthObj.setId(null);
            bankTypeAuthObj.setParentUserName(parentBankTypeAuth.getOwnerUser().getName());
            bankTypeAuthObj.setAvailableBankTypeId(parentBankTypeAuth.getBankType().getId());
            bankTypeAuthObj.setAvailableBankTypeName(parentBankTypeAuth.getBankType().getBankTypeName());
            bankTypeAuthObj.setBankTypeStatus(false);
            bankTypeAuthObj.setInitialStatus(0);
        }
        return bankTypeAuthObj;
    }

    private static BankTypeAuthObj getBankTypeAuthObj(BankType bankType, List<BankTypeAuth> bankTypeAuthList) {
        BankTypeAuthObj bankTypeAuthObj = new BankTypeAuthObj();
        boolean found = false;

        for(BankTypeAuth bankTypeAuth: bankTypeAuthList){
            if(bankType.equals(bankTypeAuth.getBankType())){
                bankTypeAuthObj.setId(bankTypeAuth.getId());
                bankTypeAuthObj.setParentUserName(null);
                bankTypeAuthObj.setAvailableBankTypeId(bankTypeAuth.getBankType().getId());
                bankTypeAuthObj.setAvailableBankTypeName(bankTypeAuth.getBankType().getBankTypeName());
                bankTypeAuthObj.setBankTypeStatus(bankTypeAuth.isBankTypeStatus());
                bankTypeAuthObj.setInitialStatus(bankTypeAuth.getInitStatus());
                found = true;
                break;
            }

        }
        if(!found) {
            bankTypeAuthObj.setId(null);
            bankTypeAuthObj.setParentUserName(null);
            bankTypeAuthObj.setAvailableBankTypeId(bankType.getId());
            bankTypeAuthObj.setAvailableBankTypeName(bankType.getBankTypeName());
            bankTypeAuthObj.setBankTypeStatus(false);
            bankTypeAuthObj.setInitialStatus(0);
        }
        return bankTypeAuthObj;
    }

    @Override
    @Transactional
    public BankTypeAuthResponse changeStatusBankTypeAuth(String ar7Id,BankTypeAuthRequest data) {
        User currentUser = userService.findByAr7Id(ar7Id);
        BankType bankType = findById(data.getBankTypeId());
        BankTypeAuth bankTypeAuth = bankTypeAuthRepo.findByOwnerUserAndBankType(currentUser,bankType).orElseThrow(
                () -> new DataNotFoundException("Hello User This Auth Object can't change because of not provided of you")
        );

        bankTypeAuth.setBankTypeStatus(!bankTypeAuth.isBankTypeStatus());
        bankTypeAuthRepo.save(bankTypeAuth);

        updateBankTypeStatusForUsers(bankTypeAuth,currentUser);

        return BankTypeAuthResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Change Bank Type Status")
                .status(true)
                .bankTypeAuthObj(objectMapper.mapToBankTypeAuthObj(bankTypeAuth))
                .build();
    }

    @Override
    public BankTypeAuthResponse getParentBankTypeAuthList() {
        String ar7Id = ContextUtils.getAr7IdFromContext();
        User currentUser = userService.findByAr7Id(ar7Id);
        List<BankTypeAuth> bankTypeAuthList = bankTypeAuthRepo.findByOwnerUser_Ar7IdAndInitStatusAndBankTypeStatusIsTrue(currentUser.getParentUserId(),1);
        return BankTypeAuthResponse.builder()
                .status(true)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .bankTypeAuthObjList(bankTypeAuthList.stream().map(objectMapper::mapToBankTypeAuthObj).toList())
                .build();
    }

    private void updateBankTypeStatusForUsers(BankTypeAuth bankTypeAuth, User currentUser) {
        List<User> downLineUsers = getAllDownLineUsers(currentUser);
        List<BankTypeAuth> updatedBankTypeAuths = downLineUsers.stream()
                .map(user -> findByOwnerUserAndBankType(user, bankTypeAuth.getBankType()))
                .filter(Objects::nonNull)
                .map(auth -> {
                    auth.setBankTypeStatus(!auth.isBankTypeStatus());
                    return auth;
                })
                .toList();
        List<BankNameAuth> bankNameAuthList = downLineUsers.stream()
                .map(User::getBankNameAuthList)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(bankNameAuth -> {
                    bankNameAuth.setBankNameStatus(!bankNameAuth.getBankNameStatus());
                    return bankNameAuth;
                }).toList();

        List<BankAcc> bankAccList = downLineUsers.stream()
                .map(User::getBankAccount)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(bankAcc -> {
                    bankAcc.setAccountStatus(!bankAcc.isAccountStatus());
                    return bankAcc;
                })
                .toList();

        if (!updatedBankTypeAuths.isEmpty()) {
            bankTypeAuthRepo.saveAll(updatedBankTypeAuths);
            bankNameAuthRepo.saveAll(bankNameAuthList);
            bankAccRepo.saveAll(bankAccList);
        }
    }

    private List<User> getAllDownLineUsers(User currentUser) {
        List<User> allUsers = new ArrayList<>();
        List<User> currentLevelUsers = getDownLineUserList(currentUser);

        while (!currentLevelUsers.isEmpty() ) {
            allUsers.addAll(currentLevelUsers);
            currentLevelUsers = currentLevelUsers.stream()
                    .flatMap(user -> getDownLineUserList(user).stream())
                    .toList();
        }

        return allUsers;
    }

    private List<User> getDownLineUserList(User currentUser) {
        if(currentUser.getRole() == Role.AGENT){
            return new ArrayList<>();
        }
        return userRepository.findByParentUserId(currentUser.getAr7Id());
    }

    private BankTypeAuth findByOwnerUserAndBankType(User user, BankType bankType){
        return bankTypeAuthRepo.findByOwnerUserAndBankType(user, bankType).orElse(null);
    }
}
