package com.security.spring.user.service;

import com.security.spring.change_password.dto.ChangeAr7IdResponse;
import com.security.spring.rro.ResponseFormat;
import com.security.spring.user.dto.*;
import com.security.spring.user.entity.User;
import com.security.spring.user.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
     User saveUser(User saveUser);
     Page<UserResponseObj> getAllUser(Pageable pageable);
     Page<UserResponseObj> getUserByRole(Role role, String searchData, Pageable pageable);
     Page<UserResponseObj> getBanUserAll(String searchData, Pageable pageable);
     UserResponse getDownLineUserByUpLineId(String ar7Id, Pageable pageable, String searchData);
     UserResponse getUserByAR7Id(String ar7Id);
     ParentUserUpdateResponse updateParentUser(ParentUserRequest data,String ar7Id);
     UserResponse banStatusChange(BanStatusRequest data);
     ResponseFormat checkAr7Id(String ar7Id,String secretCode);

    User findByAr7Id(String agentAr7Id);

    List<User> findByParentUserId(String ar7Id);

    List<User> findByParentUserIdAndRoleAndStatus(String ar7Id, Role role, boolean b);


    ChangeAr7IdResponse changeAr7IdByOwner(String newAr7Id);

    UserResponse editProfile(String ar7Id, ProfileEditRequest request);
}
