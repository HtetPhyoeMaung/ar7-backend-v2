package com.security.spring.change_password.service;

import com.security.spring.change_password.dto.ChangePasswordRequest;
import com.security.spring.change_password.dto.ChangePasswordResponse;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.FieldRequireException;
import com.security.spring.exceptionall.InvalidException;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;

@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request, String userAr7Id) throws NoPermissionException {
        if(checkByChangeType(request.getByChange())){
            return changeByThemSelf(request, userAr7Id);
        }
        return changeByAdmin(request, userAr7Id);
    }

    private ChangePasswordResponse changeByThemSelf(ChangePasswordRequest request, String userAr7Id){
        User user = userRepository.findByAr7Id(userAr7Id)
                .orElseThrow(() -> new DataNotFoundException("user does not exit by ar7Id : "+userAr7Id));

        if (request.getCurrentPassword() == null){
            throw new FieldRequireException("current password is must not be null or empty");
        }
        if(!validatePassword(request.getCurrentPassword(), user.getPassword())){
            throw new InvalidException("Current password is not valid.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user = userRepository.save(user);

        return ChangePasswordResponse.builder()
                .ar7Id(user.getAr7Id())
                .changedPassword(user.getPassword())
                .message("Successfully changed password by yourself.")
                .byChange(ChangePasswordRequest.ByChange.BY_THEMSELF)
                .status(true)
                .build();
    }

    private ChangePasswordResponse changeByAdmin(ChangePasswordRequest request, String adminAr7Id) throws NoPermissionException {
        User user = userRepository.findByAr7Id(adminAr7Id)
                .orElseThrow(() -> new DataNotFoundException("user not found by ar7Id : "+ adminAr7Id));
        if(!checkAdminOrNot(user)){
            throw new NoPermissionException("your role are not permission to change user's password");
        }

        User toUpdateUser = userRepository.findByAr7Id(request.getUserAr7Id())
                .orElseThrow(() -> new DataNotFoundException("toUpdateUser not found by ar7Id : "+ request.getUserAr7Id()));

        toUpdateUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        toUpdateUser = userRepository.save(toUpdateUser);

        return ChangePasswordResponse.builder()
                .ar7Id(toUpdateUser.getAr7Id())
                .changedPassword(toUpdateUser.getPassword())
                .message("Successfully changed password by Admin.")
                .byChange(ChangePasswordRequest.ByChange.BY_ADMIN)
                .byAdminId(user.getAr7Id())
                .status(true)
                .build();
    }

    private boolean checkByChangeType(ChangePasswordRequest.ByChange byChangeType){
        return byChangeType.equals(ChangePasswordRequest.ByChange.BY_THEMSELF);
    }

    private boolean checkAdminOrNot(User user){
        return user.getRole().equals(Role.ADMIN);
    }

    private boolean validatePassword(String currentPassword, String encodedPassword){
        return passwordEncoder.matches(currentPassword, encodedPassword);
    }
}
