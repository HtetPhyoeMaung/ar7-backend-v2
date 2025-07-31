package com.security.spring.user.service;

import com.security.spring.change_password.dto.ChangeAr7IdResponse;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.notification.dto.NotificationResponse;
import com.security.spring.notification.entity.Notification;
import com.security.spring.notification.repository.NotificationRepo;
import com.security.spring.promotion.dto.TicketBoxResponse;
import com.security.spring.promotion.entity.TicketBox;
import com.security.spring.rro.ResponseFormat;
import com.security.spring.user.dto.*;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.role.Role;
import com.security.spring.utils.ContextUtils;
import com.security.spring.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepo;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepo notificationRepo;

    @Override
    @Transactional
    public User saveUser(User saveUser) {
        User user = userRepo.save(saveUser);
        return user;
    }

    @Override
    @Transactional
    public Page<UserResponseObj> getAllUser(Pageable pageable) {
        Page<User> userPage = userRepo.findAll(pageable);
        return userPage.map(data ->{
            UserResponseObj userResponseObj = UserResponseObj.builder()
                .userName(data.getName())
                .userEmail(data.getEmail())
                .ar7Id(data.getAr7Id())
                .role(data.getRole())
                .lastLoginTime(data.getLoginTime())
                .profileImage(null)
                .userStatus(data.getStatus())
                .userUnits(data.getUserUnits())
//                .userComession(data.getUserComession())
                .secretCode(data.getSecretCode())
                .parentUserId(data.getParentUserId())
                .build();
            if(data.getTicketBox() != null){
                TicketBox ticketBox = data.getTicketBox();
                TicketBoxResponse ticketBoxResponse = TicketBoxResponse.builder()
                        .id(ticketBox.getId())
                        .ticketAmount(ticketBox.getTicketAmount())
                        .outlierValue(ticketBox.getOutlierValue())
                        .build();
                userResponseObj.setTicketBoxResponse(ticketBoxResponse);
            }
            return userResponseObj;
        });
    }

    @Override
    @Transactional
    public Page<UserResponseObj> getUserByRole(Role role, String searchData, Pageable pageable) {
        Page<User> userPage;
        if(searchData == null){
            userPage = userRepo.findByRole(role,pageable);
        }else {
            userPage = userRepo.findByRoleAndAr7IdContainingIgnoreCase(role,searchData,pageable);
        }

        return userPage.map(data ->{
            UserResponseObj userResponseObj = UserResponseObj.builder()
                    .userName(data.getName())
                    .userEmail(data.getEmail())
                    .ar7Id(data.getAr7Id())
                    .role(data.getRole())
                    .lastLoginTime(data.getLoginTime())
                    .profileImage(null)
                    .userStatus(data.getStatus())
                    .userUnits(data.getUserUnits())
//                .userComession(data.getUserComession())
                    .secretCode(data.getSecretCode())
                    .parentUserId(data.getParentUserId())
                    .build();
            if(data.getTicketBox() != null){
                TicketBox ticketBox = data.getTicketBox();
                TicketBoxResponse ticketBoxResponse = TicketBoxResponse.builder()
                        .id(ticketBox.getId())
                        .ticketAmount(ticketBox.getTicketAmount())
                        .outlierValue(ticketBox.getOutlierValue())
                        .build();
                userResponseObj.setTicketBoxResponse(ticketBoxResponse);
            }
            return userResponseObj;
        });
    }

    @Override
    @Transactional
    public Page<UserResponseObj> getBanUserAll(String searchData, Pageable pageable) {
        Page<User> userPage;
        if(searchData == null){
            userPage = userRepo.findByStatusIsFalse(pageable);
        }else {
            userPage = userRepo.findByStatusIsFalseAndAr7IdContainingIgnoreCase(searchData, pageable);
        }
        return userPage.map(data ->{
            UserResponseObj userResponseObj = UserResponseObj.builder()
                    .userName(data.getName())
                    .userEmail(data.getEmail())
                    .ar7Id(data.getAr7Id())
                    .role(data.getRole())
                    .lastLoginTime(data.getLoginTime())
                    .profileImage(null)
                    .userStatus(data.getStatus())
                    .userUnits(data.getUserUnits())
//                .userComession(data.getUserComession())
                    .secretCode(data.getSecretCode())
                    .parentUserId(data.getParentUserId())
                    .build();
            if(data.getTicketBox() != null){
                TicketBox ticketBox = data.getTicketBox();
                TicketBoxResponse ticketBoxResponse = TicketBoxResponse.builder()
                        .id(ticketBox.getId())
                        .ticketAmount(ticketBox.getTicketAmount())
                        .outlierValue(ticketBox.getOutlierValue())
                        .build();
                userResponseObj.setTicketBoxResponse(ticketBoxResponse);
            }
            return userResponseObj;
        });
    }

    @Override
    @Transactional
    public UserResponse getDownLineUserByUpLineId(String ar7Id, Pageable pageable, String searchData) {
        Page<User> userPage;
        if(searchData == null){
            userPage = userRepo.findByParentUserId(ar7Id,pageable);
        }else{
            userPage = userRepo.findByParentUserIdAndAr7IdContainingIgnoreCase(ar7Id,searchData,pageable);
        }

        List<UserResponseObj> userResponseObjList = userPage.map(data ->{
            UserResponseObj userResponseObj = UserResponseObj.builder()
                    .userName(data.getName())
                    .userEmail(data.getEmail())
                    .ar7Id(data.getAr7Id())
                    .role(data.getRole())
                    .lastLoginTime(data.getLoginTime())
                    .profileImage(null)
                    .userStatus(data.getStatus())
                    .userUnits(data.getUserUnits())
//                .userComession(data.getUserComession())
                    .secretCode(data.getSecretCode())
                    .parentUserId(data.getParentUserId())
                    .build();
            if(data.getTicketBox() != null){
                TicketBox ticketBox = data.getTicketBox();
                TicketBoxResponse ticketBoxResponse = TicketBoxResponse.builder()
                        .id(ticketBox.getId())
                        .ticketAmount(ticketBox.getTicketAmount())
                        .outlierValue(ticketBox.getOutlierValue())
                        .build();
                userResponseObj.setTicketBoxResponse(ticketBoxResponse);
            }
            return userResponseObj;
        }).toList();

        return UserResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .userResponseList(userResponseObjList)
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .currentPage(userPage.getNumber())
                .pageSize(userPage.getSize())
                .build();
    }

    @Override
    @Transactional
    public UserResponse getUserByAR7Id(String ar7Id) {
       User returnUser = userRepo.findByAr7Id(ar7Id)
               .orElseThrow(()->new DataNotFoundException("user Not Found"));

       UserResponseObj userResponseObj = UserResponseObj
               .builder()
               .userName(returnUser.getName())
               .userEmail(returnUser.getEmail())
               .userPhone(returnUser.getPhone())
               .ar7Id(returnUser.getAr7Id())
               .role(returnUser.getRole())
               .lastLoginTime(returnUser.getLoginTime())
               .profileImage(null)
               .userStatus(returnUser.getStatus())
               .userUnits(returnUser.getUserUnits())
               .parentUserId(returnUser.getParentUserId())
               .build();

       if(returnUser.getTicketBox() != null){
           TicketBox ticketBox = returnUser.getTicketBox();
           TicketBoxResponse ticketBoxResponse = TicketBoxResponse.builder()
                   .id(ticketBox.getId())
                   .ticketAmount(ticketBox.getTicketAmount())
                   .outlierValue(ticketBox.getOutlierValue())
                   .build();
           userResponseObj.setTicketBoxResponse(ticketBoxResponse);
       }

       List<UserResponseObj> userResponseObjList = new ArrayList<>();
       userResponseObjList.add(userResponseObj);

       return UserResponse
               .builder()
               .message("API Working")
               .statusCode(HttpStatus.OK.value())
               .status(true)
               .userResponseList(userResponseObjList)
               .build();
    }

    @Transactional
    @Override
    public UserResponse banStatusChange(BanStatusRequest data){
        String ar7Id = ContextUtils.getAr7IdFromContext();
        User user  = userRepo.findByAr7Id(ar7Id).orElseThrow(() -> new DataNotFoundException("current user not found by id +"+ar7Id));
        Optional<User> banUser = userRepo.findByAr7Id(data.getAr7Id());
        if(banUser.isEmpty()){
            throw new DataNotFoundException("User Not Found");
        }
        User banUserGet = banUser.get();
        if(user.getRole().equals(banUserGet.getRole())){
            throw new UnauthorizedException("You have not permission to ban your same role "+user.getRole());
        }


        banUserGet.setStatus(!banUserGet.getStatus());
        banUserGet = userRepo.save(banUserGet);

        Notification notification = Notification.builder()
                .senderId(user.getAr7Id())
                .receiverId(banUserGet.getAr7Id())
                .type(Notification.Type.BAN_STATUS)
                .message(banUserGet.getStatus() ? "You have been unbanned from your upLine user. " : "You have been banned from your upLine user")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();
        notification = notificationRepo.save(notification);
        NotificationResponse notificationResponse = MapperUtil.mapToNotificationResponse(notification);
        simpMessagingTemplate.convertAndSendToUser(notificationResponse.getReceiverId(),"/queue/notification",notificationResponse);

        return UserResponse
                .builder()
                .message("User Status Change Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

//    @Transactional
//    @Override
//    public ProfileResponse unBanUser(String ar7Id){
//        User user = userRepo.findByAr7Id(ar7Id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        user.setStatus(true);
//        userRepo.save(user);
//        return mapToProfileResponse(user);
//    }

//    @Override
//    public User getUserObjByAr7Id(String ar7Id) {
//        User returnUser = userRepo.findByAr7Id(ar7Id)
//                .orElseThrow(() -> new DataNotFoundException("User not found"));
//        return returnUser;
//    }

    @Override
    @Transactional
    public ResponseFormat checkAr7Id(String ar7Id,String secretCode) {
        String dataOfAR7id = ar7Id;
        String dataOfSecretCode = secretCode;
        User returnUser = userRepo.findByAr7Id(dataOfAR7id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        ResponseFormat responseFormat = new ResponseFormat();
        if (returnUser.getSecretCode().equals(dataOfSecretCode)) {
            responseFormat.setMessage("Secret Code are correct");
            responseFormat.setStatusCode(200);
            responseFormat.setStatus(true);
        }else{
            responseFormat.setMessage("Secret Code are not correct");
            responseFormat.setStatusCode(403);
            responseFormat.setStatus(false);
        }
        return responseFormat;
    }

    @Override
    public User findByAr7Id(String ar7Id) {
        return userRepo.findByAr7Id(ar7Id).orElseThrow(()->
                new DataNotFoundException("User not found by "+ar7Id));
    }

    @Override
    public List<User> findByParentUserId(String ar7Id) {
        return userRepo.findByParentUserId(ar7Id);
    }

    @Override
    public List<User> findByParentUserIdAndRoleAndStatus(String ar7Id, Role role, boolean b) {
        return userRepo.findByParentUserIdAndRoleAndStatus(ar7Id, role, b);
    }

    @Override
    public ChangeAr7IdResponse changeAr7IdByOwner(String newAr7Id) {
        String oldAr7Id = ContextUtils.getAr7IdFromContext();
        String role = findByAr7Id(oldAr7Id).getRole().name();
        if (userRepo.existsByAr7Id(newAr7Id)){
            throw new CustomAlreadyExistException("This ar7Id's already exists.");
        }
        if(role.equals(Role.USER.name())){
            throw new UnauthorizedException("User can't update ar7Id.");
        }

        if(!newAr7Id.startsWith(oldAr7Id.substring(0,2))){
            throw new UnauthorizedException("new ar7Id must start with "+oldAr7Id.substring(0,2));
        }
        User user = findByAr7Id(oldAr7Id);
        user.setAr7Id(newAr7Id);
        User updatedUser = userRepo.save(user);

        List<User> downLineUserList = userRepo.findByParentUserId(oldAr7Id).stream().peek(downLineUser -> downLineUser.setParentUserId(updatedUser.getAr7Id())).toList();
        userRepo.saveAll(downLineUserList);

        return ChangeAr7IdResponse.builder()
                .status(true)
                .message("successfully updated ar7Id.")
                .oldAr7Id(oldAr7Id)
                .newAr7Id(updatedUser.getAr7Id())
                .build();
    }

    @Override
    public UserResponse editProfile(String ar7Id, ProfileEditRequest request) {
        User user = findByAr7Id(ar7Id);
        user = edit(request, user);
        UserResponseObj userResponseObj = UserResponseObj
                .builder()
                .userName(user.getName())
                .userEmail(user.getEmail())
                .userPhone(user.getPhone())
                .ar7Id(user.getAr7Id())
                .role(user.getRole())
                .lastLoginTime(user.getLoginTime())
                .profileImage(null)
                .userStatus(user.getStatus())
                .userUnits(user.getUserUnits())
                .secretCode(user.getSecretCode())
                .parentUserId(user.getParentUserId())
                .build();

        if(user.getTicketBox() != null){
            TicketBox ticketBox = user.getTicketBox();
            TicketBoxResponse ticketBoxResponse = TicketBoxResponse.builder()
                    .id(ticketBox.getId())
                    .ticketAmount(ticketBox.getTicketAmount())
                    .outlierValue(ticketBox.getOutlierValue())
                    .build();
            userResponseObj.setTicketBoxResponse(ticketBoxResponse);
        }
        List<UserResponseObj> userResponseObjList = new ArrayList<>();
        userResponseObjList.add(userResponseObj);

        return UserResponse
                .builder()
                .message("API Working")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .userResponseList(userResponseObjList)
                .build();
    }

    private User edit(ProfileEditRequest request, User user) {
        if(request.getEmail() == null && request.getName() == null && request.getPhone() == null){
            throw new UnauthorizedException("when edit profile , you must edit at least one field.");
        }
        if(request.getName() != null){
            user.setName(request.getName());
        }
        if(request.getEmail() != null){
            user.setEmail(request.getEmail());
        }
        if(request.getPhone() != null){
            user.setPhone(request.getPhone());
        }
        return userRepo.save(user);
    }

    private ParentUserUpdateResponse createErrorResponse(String message) {
        return ParentUserUpdateResponse
                .builder()
                .status(false)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }

    @Override
    @Transactional
    public ParentUserUpdateResponse updateParentUser(ParentUserRequest data, String ar7Id) {
        Optional<User> currentUser = userRepo.findByAr7Id(ar7Id);
        User currentUserGet = currentUser.get();

        if(currentUserGet.getRole().equals(Role.ADMIN)){
            throw new DataNotFoundException("Hay U are Admin you can't have your parent because of you are top level");
        }

        if(currentUserGet.getRole().equals(Role.SENIORMASTER)){
            throw new DataNotFoundException("Hello Senior Master your Level can't change Parent User");
        }

        Optional<User> parentUser = userRepo.findByAr7Id(data.getParentAr7Id());
        if(parentUser.isEmpty()){
           throw new DataNotFoundException("Hay Your Parent User is not found, Please Choose another user");
        }

        User parentUserGet = parentUser.get();
        String parentUserRole = String.valueOf(parentUserGet.getRole());
        String currentUserRole = String.valueOf(currentUserGet.getRole());

        switch (Role.valueOf(currentUserRole)) {
            case USER:
                if (!parentUserRole.equals("AGENT")) {
                    return createErrorResponse("User can only have Agent as parent");
                }
                break;

            case AGENT:
                if (!parentUserRole.equals("MASTER")) {
                    return createErrorResponse("Agent can only have Master as parent");
                }
                break;

            case MASTER:
                if (!parentUserRole.equals("SENIORMASTER")) {
                    return createErrorResponse("Master can only have Senior Master as parent");
                }
                break;

            case SENIORMASTER:
                if (!parentUserRole.equals("ADMIN")) {
                    return createErrorResponse("Senior Master can only have Admin as parent");
                }
                break;
        }

        if(parentUserGet.getStatus()){
            currentUserGet.setParentUserId(data.getParentAr7Id());
            userRepo.save(currentUserGet);
            return ParentUserUpdateResponse
                    .builder()
                    .message("API Successfully Connected Parent User ")
                    .statusCode(HttpStatus.OK.value())
                    .status(true)
                    .build();
        }else{
            throw new DataNotFoundException("Your Parent User was temporatory ban");
        }
    }

}
