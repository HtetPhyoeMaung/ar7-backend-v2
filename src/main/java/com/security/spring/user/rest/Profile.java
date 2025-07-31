package com.security.spring.user.rest;

import com.security.spring.auth.AuthenticationService;
import com.security.spring.config.JWTService;
import com.security.spring.user.role.Role;
import com.security.spring.user.dto.*;
import com.security.spring.user.service.UserService;
import com.security.spring.utils.ContextUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class Profile {
    private final JWTService jwtService;
    private final UserService userServiceImpl;
    private final AuthenticationService authenticationService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        UserResponse user = userServiceImpl.getUserByAR7Id(ar7Id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> editProfile(@RequestBody ProfileEditRequest request,
                                                    @RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        UserResponse user = userServiceImpl.editProfile(ar7Id, request);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/allUser")
    public ResponseEntity<UserResponse> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"userId");
        Page<UserResponseObj> userPage = userServiceImpl.getAllUser(pageable);

        UserResponse response = UserResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .userResponseList(userPage.getContent())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .currentPage(userPage.getNumber())
                .pageSize(userPage.getSize())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/userByRole")
    public ResponseEntity<UserResponse> getUserByRole(
            @RequestParam Role role,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam(value = "searchData", required = false) String searchData
    ){
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"userId");
        Page userPage = userServiceImpl.getUserByRole(role,searchData,pageable);

        UserResponse response = UserResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .userResponseList(userPage.getContent())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .currentPage(userPage.getNumber())
                .pageSize(userPage.getSize())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/banUser")
    public ResponseEntity<UserResponse> getBanUser(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam(value = "searchData", required = false) String searchData
    ){
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"userId");
        Page userPage = userServiceImpl.getBanUserAll(searchData,pageable);

        UserResponse response = UserResponse.builder()
                .message("API Work Successfully")
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .userResponseList(userPage.getContent())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .currentPage(userPage.getNumber())
                .pageSize(userPage.getSize())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/downline/{upLineId}")
    public ResponseEntity<UserResponse> getDownLineUserListByUpLineId(
            @PathVariable String upLineId,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam(value = "searchData", required = false) String searchData
    ){
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"userId");
        UserResponse userResponse = userServiceImpl.getDownLineUserByUpLineId(upLineId,pageable, searchData);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/downline")
    public ResponseEntity<UserResponse> getDownLineUserList(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(value = "searchData", required = false) String searchData){
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"userId");
        String ar7Id = ContextUtils.getAr7IdFromContext();
        UserResponse userResponse = userServiceImpl.getDownLineUserByUpLineId(ar7Id,pageable,searchData);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/parentUserUpdate")
    public ResponseEntity<?> updateParentUser(@RequestBody @Valid ParentUserRequest data, @RequestHeader("Authorization") String token){

        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);

        ParentUserUpdateResponse resObj = userServiceImpl.updateParentUser(data,ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/profile/{ar7Id}")
    public ResponseEntity<UserResponse> getOtherProfile(@PathVariable String ar7Id) {
        UserResponse user = userServiceImpl.getUserByAR7Id(ar7Id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/banstatus")
    public ResponseEntity<?> userBanStatus(@RequestBody BanStatusRequest data){
        UserResponse resObj = userServiceImpl.banStatusChange(data);
        return ResponseEntity.ok().body(resObj);
    }
}
