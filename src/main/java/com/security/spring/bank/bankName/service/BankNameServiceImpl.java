package com.security.spring.bank.bankName.service;

import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankAcc.repo.BankAccRepo;
import com.security.spring.bank.bankName.dto.*;
import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import com.security.spring.bank.bankName.repo.BankNameAuthRepo;
import com.security.spring.bank.bankName.repo.BankNameRepo;
import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.bank.bankType.entity.BankTypeAuth;
import com.security.spring.bank.bankType.repo.BankTypeAuthRepo;
import com.security.spring.bank.bankType.repo.BankTypeRepo;
import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.imageConfig.FileUploadService;
import com.security.spring.user.role.Role;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.Constraint;
import com.security.spring.utils.ContextUtils;
import com.security.spring.utils.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankNameServiceImpl implements BankNameService{

    private final BankTypeRepo bankTypeRepo;
    private final BankNameRepo bankNameRepo;
    private final FileUploadService fileUploadService;
    private final BankTypeAuthRepo bankTypeAuthRepo;
    private final UserRepository userRepository;
    private final BankNameAuthRepo bankNameAuthRepo;
    private final BankAccRepo bankAccRepo;
    private final JWTService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    private String imageFolderName = "bankName/";

    @Override
    @Transactional
    public BankNameResponse saveBankName(BankNameRequest data) {
        Optional<BankType> bankTypeObj = bankTypeRepo.findById(Long.valueOf(data.getBankTypeId()));
        if(bankTypeObj.isEmpty()){
            throw new DataNotFoundException("BankType not Found");
        }
        BankType bankTypeGet = bankTypeObj.get();

        String imageUrl = fileUploadService.uploadFile(data.getBankNameImage(),data.getBankName(),imageFolderName);

        var bankNameObj = BankName
                .builder()
                .bankType(bankTypeGet)
                .bankName(data.getBankName())
                .bankNameImageUrl(imageUrl)
                .build();

        bankNameRepo.save(bankNameObj);

        return BankNameResponse
                .builder()
                .bankNameObjList(null)
                .message("Bank Name " + data.getBankName() + "created successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .build();

    }

    @Override
    public BankNameResponse getBankNameAll(String token) {
        List<BankNameObj> bankNameObj;
        String ar7Id = jwtService.extractUsername(token.substring(7));
        if (ar7Id.startsWith("A") || ar7Id.startsWith("S") || ar7Id.startsWith("M")){
            List<BankName> bankNameList = bankNameRepo.findAll();
            bankNameObj = bankNameList
                    .stream()
                    .map( obj -> BankNameObj
                            .builder()
                            .id(obj.getId())
                            .bankTypeId(obj.getBankType().getId())
                            .bankTypeName(obj.getBankType().getBankTypeName())
                            .bankLogoUrl(obj.getBankNameImageUrl())
                            .bankName(obj.getBankName())

                            .build())
                    .toList();
        }else {
            List<BankName> bankNameList = bankNameRepo.findByBankType_bankTypeName("Online Payment");
            bankNameObj = bankNameList
                    .stream()
                    .map( obj -> BankNameObj
                            .builder()
                            .id(obj.getId())
                            .bankTypeId(obj.getBankType().getId())
                            .bankTypeName(obj.getBankType().getBankTypeName())
                            .bankLogoUrl(obj.getBankNameImageUrl())
                            .bankName(obj.getBankName())

                            .build())
                    .toList();
        }


        return BankNameResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("API Work Success")
                .bankNameObjList(bankNameObj)
                .build();
    }

    @Override
    public BankNameResponse getBankNameById(Long id) {
        Optional<BankName> bankName = bankNameRepo.findById(id);
        if(!bankName.isPresent()){
            return BankNameResponse
                    .builder()
                    .bankNameObjList(null)
                    .message("Your Bank Name Id is Invalid")
                    .status(false)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        var bankNameObj = BankNameObj
                .builder()
                .id(bankName.get().getId())
                .bankTypeId(bankName.get().getBankType().getId())
                .bankTypeName(bankName.get().getBankType().getBankTypeName())
                .bankName(bankName.get().getBankName())
                .bankLogoUrl(bankName.get().getBankNameImageUrl())
                .build();

        List<BankNameObj> bankNameObjs = new ArrayList<>();
        bankNameObjs.add(bankNameObj);

        return BankNameResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("API Work Success")
                .bankNameObjList(bankNameObjs)
                .build();
    }

    @Override
    public BankNameResponse updateBankName(BankNameRequest data) {
        // Check if bank name exists
        Optional<BankName> bankName = bankNameRepo.findById(Long.valueOf(data.getId()));
        if(!bankName.isPresent()){
            return BankNameResponse
                    .builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(false)
                    .message("Please Add Correct Bank Name Id")
                    .bankNameObjList(null)
                    .build();
        }

        BankName bankNameGet = bankName.get();

        // Update bank name if provided
        if(data.getBankName() != null && !data.getBankName().isEmpty()) {
            bankNameGet.setBankName(data.getBankName());
        }

        // Update bank type if provided
        if(data.getBankTypeId() != null) {
            Optional<BankType> bankType = bankTypeRepo.findById(Long.valueOf(data.getBankTypeId()));
            if(!bankType.isPresent()){
                return BankNameResponse
                        .builder()
                        .bankNameObjList(null)
                        .message("Your Bank Type Id is Invalid")
                        .status(false)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build();
            }
            BankType bankTypeGet = bankType.get();
            bankNameGet.setBankType(bankTypeGet);
        }

        // Update image if new image is provided
        if(data.getBankNameImage() != null && !data.getBankNameImage().isEmpty()) {
            try {
                // Delete old image if exists
                if(bankNameGet.getBankNameImageUrl() != null && !bankNameGet.getBankNameImageUrl().isEmpty()) {
                    boolean isDeleted = fileUploadService.deleteFile(bankNameGet.getBankNameImageUrl());
                    if(!isDeleted) {
                        return BankNameResponse
                                .builder()
                                .bankNameObjList(null)
                                .message("Failed to delete old image")
                                .status(false)
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .build();
                    }
                }

                // Upload new image
                String newImageUrl = fileUploadService.uploadFile(data.getBankNameImage(), data.getBankName(), imageFolderName);
                if(newImageUrl == null || newImageUrl.isEmpty()) {
                    return BankNameResponse
                            .builder()
                            .bankNameObjList(null)
                            .message("Failed to upload new image")
                            .status(false)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build();
                }

                bankNameGet.setBankNameImageUrl(newImageUrl);

            } catch (Exception e) {
                return BankNameResponse
                        .builder()
                        .bankNameObjList(null)
                        .message("Error processing image: " + e.getMessage())
                        .status(false)
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build();
            }
        }

        try {
            // Save updated bank name
            bankNameRepo.save(bankNameGet);

            return BankNameResponse
                    .builder()
                    .bankNameObjList(null)
                    .message("Bank Name updated successfully")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();

        } catch (Exception e) {
            return BankNameResponse
                    .builder()
                    .bankNameObjList(null)
                    .message("Error saving bank name: " + e.getMessage())
                    .status(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }

    @Override
    @Transactional
    public BankNameAuthResponse saveBankNameAuth(BankNameAuthRequest data, String ar7Id) {

        User currentUser = userService.findByAr7Id(ar7Id);

        BankType bankType = bankTypeRepo.findById(Long.valueOf(data.getBankTypeId()))
                .orElseThrow(() -> new DataNotFoundException("Bank Type Not Found"));

        BankName bankName = bankNameRepo.findById(Long.valueOf(data.getBankNameId()))
                .orElseThrow(() -> new DataNotFoundException("Bank Name Not Found"));
        log.info("Bank Name : {}",bankName);
        if(currentUser.getRole().equals(Role.ADMIN)){
            BankTypeAuth bankTypeAuth = bankTypeAuthRepo.findByOwnerUserAndBankTypeAndBankTypeStatusIsTrue(currentUser, bankType).orElseThrow(()->
                    new DataNotFoundException("BankTypeAuth not found!"));
            BankNameAuth bankNameAuth = bankNameAuthRepo.findByOwnerUserAndBankName(currentUser,bankName).orElse(null);
            if (bankNameAuth!=null){
                throw new CustomAlreadyExistException("This Bank Name is already authenticated.");
            }
            BankNameAuth saveObj = BankNameAuth
                    .builder()
                    .ownerUser(currentUser)
                    .bankType(bankType)
                    .bankName(bankName)
                    .bankNameStatus(true)
                    .initialStatus(1)
                    .build();

            bankNameAuthRepo.save(saveObj);

            return BankNameAuthResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }else{
            User parentUser = userService.findByAr7Id(currentUser.getParentUserId());
            BankNameAuth parentUserBankNameAuth = bankNameAuthRepo.findByOwnerUserAndBankNameAndBankNameStatusIsTrue(parentUser,bankName)
                    .orElseThrow(() ->new DataNotFoundException("This bankname are not authenticate by parent"));

            if(bankNameAuthRepo.findByOwnerUserAndBankName(currentUser,bankName).isPresent()){
                throw new CustomAlreadyExistException("This bankname already authenticate");
            };

            BankNameAuth saveObj = BankNameAuth
                    .builder()
                    .ownerUser(currentUser)
                    .bankType(bankType)
                    .bankName(bankName)
                    .bankNameStatus(true)
                    .initialStatus(1)
                    .build();
            bankNameAuthRepo.save(saveObj);

            return BankNameAuthResponse
                    .builder()
                    .message("API Good Working")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }

    @Override
    @Transactional
    public BankNameAuthResponse getBankNameAuth(String ar7Id) {
        User currentUser = userRepository.findByAr7Id(ar7Id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<BankNameAuthObj> bankNameAuthObjList;

        if (currentUser.getRole().equals(Role.ADMIN)) {
            bankNameAuthObjList = getAdminBankAuth(currentUser);
        } else {
            bankNameAuthObjList = getUserBankAuth(currentUser);
        }

        return BankNameAuthResponse.builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("API Good Working")
                .bankNameAuthObjList(bankNameAuthObjList)
                .build();
    }

    @Override
    public BankNameAuthResponse getAuthenticatedBankNameList(String ar7id) {
        User user = userService.findByAr7Id(ar7id);
        List<BankNameAuth> bankNameAuthList = bankNameAuthRepo.findByOwnerUserAndBankNameStatusIsTrue(user);
        if (bankNameAuthList.isEmpty()){
            throw new DataNotFoundException("You don't have authenticated BankNameAuth.");
        }
        return BankNameAuthResponse.builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("API Good Working")
                .bankNameAuthObjList(bankNameAuthList.stream().map(objectMapper::mapToBankNameAuthObj).toList())
                .build();
    }

    @Override
    public BankNameAuthResponse getParentAuthenticatedBankNameList(int bankTypeId) {
        String ar7Id = ContextUtils.getAr7IdFromContext();
        User currentUser = userService.findByAr7Id(ar7Id);
        List<BankNameAuth> bankNameAuthList = bankNameAuthRepo.findByOwnerUser_Ar7IdAndBankType_IdAndInitialStatusAndBankNameStatusIsTrue(currentUser.getParentUserId(),
                bankTypeId,1);
        if (bankNameAuthList.isEmpty()){
            throw new DataNotFoundException("Parent's Bank Name aren't available.");
        }
        return BankNameAuthResponse.builder()
                .status(true)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .bankNameAuthObjList(bankNameAuthList.stream().map(objectMapper::mapToBankNameAuthObj).toList())
                .build();
    }



    private List<BankNameAuthObj> getAdminBankAuth(User adminUser) {
        List<BankTypeAuth> bankTypeAuthList = bankTypeAuthRepo.findByOwnerUserAndBankTypeStatusIsTrue(adminUser);

        List<BankName> bankNameList = bankTypeAuthList.stream()
                .map(BankTypeAuth::getBankType)
                .flatMap(bankType -> bankNameRepo.findByBankTypeId(bankType.getId()).stream())
                .collect(Collectors.toList());

        List<BankNameAuth> bankNameAuthList = bankNameAuthRepo.findByOwnerUser(adminUser);

        return bankNameList.stream()
                .map(bankName -> convertToBankNameAuthObj(bankName, bankNameAuthList))
                .collect(Collectors.toList());
    }

    private List<BankNameAuthObj> getUserBankAuth(User currentUser) {
        User parentUser = userRepository.findByAr7Id(currentUser.getParentUserId())
                .orElseThrow(() -> new DataNotFoundException("Parent user not found"));

        List<BankNameAuth> parentUserBankNameAuthList = bankNameAuthRepo.findByOwnerUserAndBankNameStatusIsTrueAndInitialStatus(parentUser,1);
        List<BankNameAuth> currentUserBankNameAuthList = bankNameAuthRepo.findByOwnerUser(currentUser);

        return parentUserBankNameAuthList.stream()
                .map(parentAuth -> convertToBankNameAuthObj(parentAuth.getBankName(), currentUserBankNameAuthList))
                .toList();
    }

    private BankNameAuthObj convertToBankNameAuthObj(BankName bankName, List<BankNameAuth> existingAuthList) {
        return existingAuthList.stream()
                .filter(auth -> auth.getBankName().getId().equals(bankName.getId()))
                .findFirst()
                .map(auth -> new BankNameAuthObj(
                        auth.getId(),
                        auth.getBankType().getId(),
                        auth.getBankType().getBankTypeName(),
                        auth.getBankName().getId(),
                        auth.getBankName().getBankName(),
                        auth.getBankNameStatus(),
                        auth.getInitialStatus(),
                        bankName.getBankNameImageUrl()
                ))
                .orElseGet(() -> new BankNameAuthObj(
                        null,
                        bankName.getBankType().getId(),
                        bankName.getBankType().getBankTypeName(),
                        bankName.getId(),
                        bankName.getBankName(),
                        false,
                        0,
                        bankName.getBankNameImageUrl()
                ));
    }


    @Override
    @Transactional
    public BankNameAuthResponse updateBankNameAuth(String ar7Id, BankNameAuthRequest data) {
        User currentUser = userService.findByAr7Id(ar7Id);

        BankNameAuth bankNameAuth = bankNameAuthRepo.findById(Long.valueOf(data.getBankNameAuthId()))
                .orElseThrow(() -> new DataNotFoundException("Bank name auth not found by bankAuthId"));

        if (!bankNameAuth.getOwnerUser().equals(currentUser)) {
            throw new DataNotFoundException("Hello User, you can't change this bank name auth");
        }

        bankNameAuth.setBankNameStatus(!bankNameAuth.getBankNameStatus());
        bankNameAuthRepo.save(bankNameAuth);

        updateBankNameStatusForUsers(bankNameAuth, currentUser);

        return BankNameAuthResponse.builder()
                .message("Bank Name Authorization Status Changed")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    private void updateBankNameStatusForUsers(BankNameAuth bankNameAuth, User currentUser) {
        List<User> downLineUsers = getAllDownLineUsers(currentUser, currentUser.getRole());
        List<BankNameAuth> updatedBankNameAuths = downLineUsers.stream()
                .map(user -> bankNameAuthRepo.findByOwnerUserAndBankTypeAndBankName(user, bankNameAuth.getBankType(), bankNameAuth.getBankName()))
                .filter(Objects::nonNull)
                .map(auth -> {
                   auth.setBankNameStatus(!auth.getBankNameStatus());
                   return auth;
                })
                .toList();

        List<BankAcc> bankAccList = downLineUsers.stream()
                .map(User::getBankAccount)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(bankAcc -> {
                    bankAcc.setAccountStatus(!bankAcc.isAccountStatus());
                    return bankAcc;
                })
                .toList();

        if (!updatedBankNameAuths.isEmpty()) {
            bankNameAuthRepo.saveAll(updatedBankNameAuths);
            bankAccRepo.saveAll(bankAccList);

        }
    }

    private List<User> getAllDownLineUsers(User currentUser, Role role) {
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

}
