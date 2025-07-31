package com.security.spring.bank.bankAcc.service;

import com.security.spring.bank.bankAcc.dto.BankAccObj;
import com.security.spring.bank.bankAcc.dto.BankAccRequest;
import com.security.spring.bank.bankAcc.dto.BankAccResponse;
import com.security.spring.bank.bankAcc.dto.BankAccStatusRequest;
import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankAcc.repo.BankAccRepo;
import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import com.security.spring.bank.bankName.repo.BankNameAuthRepo;
import com.security.spring.bank.bankName.repo.BankNameRepo;
import com.security.spring.bank.bankType.repo.BankTypeRepo;
import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.imageConfig.FileUploadService;
import com.security.spring.user.role.Role;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;

import com.security.spring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccServiceImpl implements BankAccService{


    private final UserRepository userRepository;


    private final BankNameRepo bankNameRepo;


    private final UserService userService;


    private final BankNameAuthRepo bankNameAuthRepo;


    private final BankAccRepo bankAccRepo;


    private final JWTService jwtService;


    private final BankTypeRepo bankTypeRepo;

    private String imageFolderName = "bankAccs/";

    private final FileUploadService fileUploadService;


    @Override
    @Transactional
    public BankAccResponse saveBankAcc(BankAccRequest data,String ar7Id) {

        if (bankAccRepo.existsByBankName_IdAndAccountNumber(data.getBankNameId(),data.getAccountNum())){
            throw new CustomAlreadyExistException("This bank account's already exists!");
        }

        Optional<User> currentUser = userRepository.findByAr7Id(ar7Id);
        User currentUserGet = currentUser.get();

        Optional<BankName> bankName = bankNameRepo.findById(Long.valueOf(data.getBankNameId()));
        if(bankName.isEmpty()){
            throw new DataNotFoundException("BankName Not Found");
        }
        BankName bankNameGet = bankName.get();

        String imageUrl = null;
        if (data.getQrImg() != null && !data.getQrImg().isEmpty()) {
            imageUrl = fileUploadService.uploadFile(data.getQrImg(), data.getAccountNum(), imageFolderName);
        }

        if(currentUserGet.getRole().equals(Role.ADMIN)){
            Optional<BankNameAuth> bankNameAuth = bankNameAuthRepo.findByOwnerUserAndBankNameAndBankNameStatusIsTrue(currentUserGet,bankNameGet);
            if(bankNameAuth.isEmpty()){
                throw new DataNotFoundException("This bank name is not authorize yet");
            }
            BankAcc saveObj = BankAcc
                    .builder()
                    .bankName(bankNameGet)
                    .accountName(data.getAccountName())
                    .accountNumber(data.getAccountNum())
                    .qrImgUrl(imageUrl)
                    .description(data.getDescription())
                    .accountStatus(true)
                    .ownerUser(currentUserGet)
                    .build();

            bankAccRepo.save(saveObj);

            return BankAccResponse
                    .builder()
                    .message("Bank Account Created Success")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }else{
            Optional<User> parentUser = userRepository.findByAr7Id(currentUserGet.getParentUserId());
            if(parentUser.isEmpty()){
                throw new DataNotFoundException("Hello User you haven't upline user yet.");
            }
            User parentUserGet = parentUser.get();
            Optional<BankNameAuth> parentUserBankNameAuth = bankNameAuthRepo.findByOwnerUserAndBankNameAndBankNameStatusIsTrue(parentUserGet,bankNameGet);
            if(parentUserBankNameAuth.isEmpty()){
                throw new DataNotFoundException("Hello User your upline user not permission this bankname to you");
            }
            Optional<BankNameAuth> bankNameAuth = bankNameAuthRepo.findByOwnerUserAndBankNameAndBankNameStatusIsTrue(currentUserGet,bankNameGet);
            if(bankNameAuth.isEmpty()){
                throw new DataNotFoundException("This bank name is not authorize yet");
            }

            BankAcc saveObj = BankAcc
                    .builder()
                    .bankName(bankNameGet)
                    .accountName(data.getAccountName())
                    .accountNumber(data.getAccountNum())
                    .qrImgUrl(imageUrl)
                    .description(data.getDescription())
                    .accountStatus(true)
                    .ownerUser(currentUserGet)
                    .build();

            bankAccRepo.save(saveObj);

            return BankAccResponse
                    .builder()
                    .message("Bank Account Created Success")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }

    @Override
    public BankAccResponse getBankAccById(Integer bankAccId, String ar7Id) {
        User user = userService.findByAr7Id(ar7Id);

        BankAcc bankAcc = bankAccRepo.findByIdAndOwnerUser(bankAccId, user).orElseThrow(()->
                new DataNotFoundException("This BankAcc is something wrong! Please check and Try Again"));


        BankAccObj bankAccObj = BankAccObj
                .builder()
                .bankAccId(bankAcc.getId())
                .bankAccName(bankAcc.getAccountName())
                .bankAccAccount(bankAcc.getAccountNumber())
                .bankName(bankAcc.getBankName().getBankName())
                .bankNameId(bankAcc.getBankName().getId())
                .accQrUrl(bankAcc.getQrImgUrl())
                .accountStatus(bankAcc.isAccountStatus())
                .description(bankAcc.getDescription())
                .build();

        List<BankAccObj> accObjList = new ArrayList<>();
        accObjList.add(bankAccObj);

        return BankAccResponse
                .builder()
                .bankAccObjList(accObjList)
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("API Working")
                .build();
    }

//    @Override
//    public BankAccResponse getBankAccByName(Integer bankNameId, String ar7Id) {
//        Optional<User> user = userRepository.findByAr7Id(ar7Id);
//        User userGet = user.get();
//        Optional<BankName> bankName = bankNameRepo.findById(Long.valueOf(bankNameId));
//        BankName bankNameGet = bankName.get();
//        System.out.println(userGet);
//        System.out.println(bankName.get());
//        Optional<List<BankAcc>> bankAccList = Optional.ofNullable(bankAccRepo.findByUserAndBankName(userGet, bankNameGet));
//
//        List<BankAccObj> bankAccObjList =bankAccList
//                .get()
//                .stream()
//                .map(
//                        obj -> BankAccObj
//                                .builder()
//                                .bankAccId(obj.getId())
//                                .bankAccName(obj.getAccountName())
//                                .bankAccAccount(obj.getAccountNumber())
//                                .bankName(obj.getBankName().getBankName())
//                                .bankNameId(obj.getBankName().getId())
//                                .accQrUrl(obj.getQrImgUrl())
//                                .accountStatus(obj.isAccountStatus())
//                                .description(obj.getDesscription())
//                                .build())
//                .collect(Collectors.toList());
//
//        return BankAccResponse
//                .builder()
//                .message("API Successfully")
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .bankAccObjList(bankAccObjList)
//                .build();
//    }

    @Override
    public BankAccResponse getBankAccByParent(Integer bankNameId, String ar7Id) {
        Optional<User> currentUser = userRepository.findByAr7Id(ar7Id);
        User currentUserGet = currentUser.get();

        Optional<BankName> bankName = bankNameRepo.findById(Long.valueOf(bankNameId));
        BankName bankNameGet = bankName.get();

        if(currentUserGet.getRole().equals(Role.ADMIN)){
            Optional<List<BankAcc>> bankAccList = Optional.ofNullable(bankAccRepo.findByOwnerUserAndBankNameAndAccountStatusIsTrue(currentUserGet, bankNameGet));
            List<BankAccObj> bankAccObjList =bankAccList
                    .get()
                    .stream()
                    .map(
                            obj -> BankAccObj
                                    .builder()
                                    .bankAccId(obj.getId())
                                    .bankAccName(obj.getAccountName())
                                    .bankAccAccount(obj.getAccountNumber())
                                    .bankName(obj.getBankName().getBankName())
                                    .bankNameId(obj.getBankName().getId())
                                    .accQrUrl(obj.getQrImgUrl())
                                    .accountStatus(obj.isAccountStatus())
                                    .build())
                    .collect(Collectors.toList());

            return BankAccResponse
                    .builder()
                    .message("API Successfully")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .bankAccObjList(bankAccObjList)
                    .build();

        }else{
            Optional<User> parentUser = userRepository.findByAr7Id(currentUserGet.getParentUserId());
            User parentUserGet = parentUser.get();
            Optional<List<BankAcc>> bankAccList = Optional.ofNullable(bankAccRepo.findByOwnerUserAndBankNameAndAccountStatusIsTrue(parentUserGet, bankNameGet));
            List<BankAccObj> bankAccObjList =bankAccList
                    .get()
                    .stream()
                    .map(
                            obj -> BankAccObj
                                    .builder()
                                    .bankAccId(obj.getId())
                                    .bankAccName(obj.getAccountName())
                                    .bankAccAccount(obj.getAccountNumber())
                                    .bankName(obj.getBankName().getBankName())
                                    .bankNameId(obj.getBankName().getId())
                                    .accQrUrl(obj.getQrImgUrl())
                                    .accountStatus(obj.isAccountStatus())
                                    .build())
                    .collect(Collectors.toList());

            return BankAccResponse
                    .builder()
                    .message("API Successfully")
                    .status(true)
                    .statusCode(HttpStatus.OK.value())
                    .bankAccObjList(bankAccObjList)
                    .build();
        }
    }

    @Override
    @Transactional
    public BankAccResponse getBankAccAllByParent(String ar7Id, int bankNameId) {
        User currentUser = userService.findByAr7Id(ar7Id);
        if(currentUser.getRole().equals(Role.ADMIN)){
            throw new DataNotFoundException("Hello Admin you are admin");
        }
        log.info("Parent Ar7Id : {}",currentUser.getParentUserId());

        List<BankAcc>  parentUserBankAccList = bankAccRepo.findByOwnerUser_Ar7IdAndBankName_IdAndAccountStatusIsTrue(currentUser.getParentUserId(), bankNameId);

        if (parentUserBankAccList.isEmpty()){
            throw new DataNotFoundException("Parent Bank Accounts aren't available!");
        }

        List<BankAccObj> bankAccObjList = parentUserBankAccList
                .stream()
                .map(
                        obj -> BankAccObj
                                .builder()
                                .bankAccId(obj.getId())
                                .bankAccName(obj.getAccountName())
                                .bankAccAccount(obj.getAccountNumber())
                                .bankName(obj.getBankName().getBankName())
                                .bankNameId(obj.getBankName().getId())
                                .accQrUrl(obj.getQrImgUrl())
                                .accountStatus(obj.isAccountStatus())
                                .description(obj.getDescription())
                                .build()
                )
                .toList();

        return BankAccResponse
                .builder()
                .message("API Good Working")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .bankAccObjList(bankAccObjList)
                .build();
    }

    @Override
    @Transactional
    public BankAccResponse updateBankAcc(String ar7Id, BankAccRequest data) {
        User currentUser = userService.findByAr7Id(ar7Id);
        // Find existing bank account
        BankAcc existingBankAcc = bankAccRepo.findById(Long.valueOf(data.getId())).orElseThrow(()->
                new DataNotFoundException("Bank Account Not Found"));


        if(!existingBankAcc.getOwnerUser().equals(currentUser)){
            throw new DataNotFoundException("This bank account can't change, it is not for you");
        }

        // Update only if new values are present
        if (data.getAccountName() != null) {
            existingBankAcc.setAccountName(data.getAccountName());
        }

        if (data.getAccountNum() != null) {
            existingBankAcc.setAccountNumber(data.getAccountNum());
        }

        if(data.getBankNameId() !=null){
            existingBankAcc.setBankName(bankNameRepo.findById(Long.valueOf(data.getBankNameId()))
                    .orElseThrow(()->new DataNotFoundException("Bank name not found by ID : "+data.getBankNameId())));
        }

        // Handle QR image update only if new image is provided
        if (data.getQrImg() != null) {
            // Delete old image if exists
            if (existingBankAcc.getQrImgUrl() != null) {
                fileUploadService.deleteFile(existingBankAcc.getQrImgUrl());
            }
            // Upload new image
            String newImageUrl = fileUploadService.uploadFile(
                    data.getQrImg(),
                    existingBankAcc.getAccountNumber(),
                    imageFolderName
            );
            existingBankAcc.setQrImgUrl(newImageUrl);
        }

        // Save updated entity
        bankAccRepo.save(existingBankAcc);

        return BankAccResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .message("Bank Account Updated Successfully")
                .build();
    }

    @Override
    public BankAccResponse getBankAccByOwn(String ar7Id) {
        Optional<User> currentUser = userRepository.findByAr7Id(ar7Id);
        User currentUserGet = currentUser.get();
        List<BankAcc> bankAccList = bankAccRepo.findByOwnerUser(currentUserGet);
        List<BankAccObj> bankAccObjList = bankAccList
                .stream()
                .map(
                        obj -> BankAccObj
                                .builder()
                                .bankAccId(obj.getId())
                                .bankAccName(obj.getAccountName())
                                .bankAccAccount(obj.getAccountNumber())
                                .bankName(obj.getBankName().getBankName())
                                .bankNameId(obj.getBankName().getId())
                                .accQrUrl(obj.getQrImgUrl())
                                .accountStatus(obj.isAccountStatus())
                                .build()
                )
                .collect(Collectors.toList());

        return BankAccResponse
                .builder()
                .bankAccObjList(bankAccObjList)
                .message("API Good Working")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public BankAccResponse changeBankAccStatus(String ar7Id, BankAccStatusRequest data) {
        User currentUser = userService.findByAr7Id(ar7Id);
        BankAcc bankAcc = bankAccRepo.findById(Long.valueOf(data.getBankAccId())).orElseThrow(()->
                new DataNotFoundException("Bank Acc Not Found"));

        if(!bankAcc.getOwnerUser().equals(currentUser)){
            throw new DataNotFoundException("This bank account can't change because of this is not for you");
        }
        bankAcc.setAccountStatus(!bankAcc.isAccountStatus());
        updateBankAccStatusForUsers(bankAcc,currentUser);
        var responseObj = bankAccRepo.save(bankAcc);

        BankAccObj bankAccObj = BankAccObj.builder()
                .accQrUrl(responseObj.getQrImgUrl())
                .bankAccAccount(responseObj.getAccountNumber())
                .bankAccName(responseObj.getAccountName())
                .bankAccId(responseObj.getId())
                .bankName(responseObj.getBankName().getBankName())
                .description(responseObj.getDescription())
                .accountStatus(responseObj.isAccountStatus())
                .bankNameId(responseObj.getBankName().getId())
                .build();

        return BankAccResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .bankAccObj(bankAccObj)
                .message("Success Change Bank Acc status")
                .build();
    }

    private void updateBankAccStatusForUsers(BankAcc bankAcc, User currentUser) {
        List<User> downLineUsers = getAllDownLineUsers(currentUser);
        List<BankAcc> bankAccList = downLineUsers.stream()
                .map(User::getBankAccount)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(acc -> {
                    if (acc.getBankName().getId().equals(bankAcc.getBankName().getId())){
                        acc.setAccountStatus(!acc.isAccountStatus());
                    }
                    return acc;
                })
                .toList();

        if (!bankAccList.isEmpty()){
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


}
